package edu.fer.project.questionnaire.presentation.views.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.router.RouterLink;

public class NavigationTab extends Tab {

  public NavigationTab(VaadinIcon icon, String text, Class<? extends Component> view) {
    RouterLink link = new RouterLink(null, view);
    link.add(icon.create());
    link.add(text);
    addThemeVariants(TabVariant.LUMO_ICON_ON_TOP);
    add(link);
  }
}
