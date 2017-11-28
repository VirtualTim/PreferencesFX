package com.dlsc.preferencesfx;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class CategoryTree extends TreeView {

  private List<Category> categories;
  private TreeItem<Category> rootItem;
  private HashMap<Category, TreeItem<Category>> categoryTreeItemMap = new HashMap<>();

  public CategoryTree(List<Category> categories) {
    this.categories = categories;
    setupParts();
    layoutParts();
  }

  private void setupParts() {
    rootItem = new TreeItem(Category.of("Root"));
    addRecursive(rootItem, categories);
  }

  private void addRecursive(TreeItem treeItem, List<Category> categories) {
    for (Category category : categories) {
      TreeItem<Category> item = new TreeItem<>(category);
      // If there are Subcategories, add them recursively.
      if (!Objects.equals(category.getChildren(), null)) {
        addRecursive(item, category.getChildren());
      }
      treeItem.getChildren().add(item);
      categoryTreeItemMap.put(category, item);
    }
  }

  private void layoutParts() {
    setRoot(rootItem);
    // TreeView requires a RootItem, but in this case it's not desired to have it visible.
    setShowRoot(false);
    getRoot().setExpanded(true);
    // Set initial selected category.
    getSelectionModel().select(PreferencesFx.DEFAULT_CATEGORY);
  }

  /**
   * Sets the selected item in the TreeView to the category of the given categoryId.
   * @param categoryId the id of the category to be found
   * @return the category with categoryId or the first category in the TreeView if none is found
   */
  public Category setSelectedCategoryById(int categoryId) {
    Category category = findCategoryById(categoryId);
    setSelectedItem(category);
    return category;
  }

  /**
   * Finds the category with the matching id.
   * @param categoryId the id of the category to be found
   * @return the category with categoryId or the first category in the TreeView if none is found
   */
  public Category findCategoryById(int categoryId) {
    Category selectedCategory = categoryTreeItemMap.keySet().stream().filter(
        category -> category.getId() == categoryId).findFirst()
        .orElse(rootItem.getChildren().get(0).getValue());
    return selectedCategory;
  }

  /**
   * Selects the given category in the TreeView.
   * @param category the category to be selected
   */
  public void setSelectedItem(Category category) {
    getSelectionModel().select(categoryTreeItemMap.get(category));
  }
}
