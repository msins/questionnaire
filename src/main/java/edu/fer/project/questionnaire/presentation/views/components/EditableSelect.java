package edu.fer.project.questionnaire.presentation.views.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import edu.fer.project.questionnaire.presentation.views.components.buttons.AbstractButtonFactory;

public class EditableSelect<T> extends FlexLayout {

  private Button addButton;
  private Button removeButton;
  private Select<T> select;
  private EditableSelectListener<T> listener;

  public EditableSelect(EditableSelectListener<T> listener) {
    this.listener = listener;
    initSelect();
    initAddButton();
    initRemoveButton();
    initUi();
  }

  private void initSelect() {
    select = new Select<>();
    select.setSizeUndefined();
    select.addValueChangeListener(e -> listener.onSelectionChange(e.getValue()));
  }

  private void initAddButton() {
    addButton = AbstractButtonFactory.circular().createSuccessButton(
        VaadinIcon.PLUS.create(), e -> listener.onAddClicked()
    );
  }

  private void initRemoveButton() {
    removeButton = AbstractButtonFactory.circular().createCancelButton(
        VaadinIcon.MINUS.create(), e -> listener.onRemoveClicked(select.getValue())
    );
  }

  private void initUi() {
    setAlignItems(Alignment.CENTER);
    initStyle();
    add(removeButton, select, addButton);
  }

  private void initStyle() {
    select.getStyle().set("margin-left", "1px");
    select.getStyle().set("margin-right", "1px");
    setJustifyContentMode(JustifyContentMode.CENTER);
    addButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
    removeButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
  }

  public void refresh() {
    select.getDataProvider().refreshAll();
  }

  public void setPlaceholder(String placeholder) {
    select.setPlaceholder(placeholder);
  }

  public T getValue() {
    return select.getValue();
  }

  public boolean areNoneSelected() {
    return select.getValue() == null;
  }

  public void refreshAndSet(T value) {
    select.getDataProvider().refreshAll();
    select.setValue(value);
  }

  public void setValue(T value) {
    select.setValue(value);
  }

  public void setDataProvider(DataProvider<T, ?> provider) {
    select.setDataProvider(provider);
  }

  public void setRenderer(ComponentRenderer<? extends Component, T> renderer) {
    select.setRenderer(renderer);
  }

  public interface EditableSelectListener<T> {

    void onRemoveClicked(T oldValue);

    void onSelectionChange(T value);

    void onAddClicked();
  }
}
