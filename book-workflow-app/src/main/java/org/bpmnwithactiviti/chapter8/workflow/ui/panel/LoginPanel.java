package org.bpmnwithactiviti.chapter8.workflow.ui.panel;

import java.util.Map;

import org.bpmnwithactiviti.chapter8.workflow.model.User;
import org.bpmnwithactiviti.chapter8.workflow.ui.ViewManager;

import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

public class LoginPanel extends Panel implements Handler {

	protected static final long serialVersionUID = 1L;

	private static final Action enterKeyShortcutAction = new ShortcutAction(
			null, ShortcutAction.KeyCode.ENTER, null);

	protected static final String USERNAME_FIELD = "Username";
	protected static final String PASSWORD_FIELD = "Password";
	
	// ui
	protected Label titleLabel;
	protected TextField usernameField;
	protected PasswordField passwordField;

	// dependencies
	protected ViewManager viewManager;

	public LoginPanel(ViewManager viewManager) {
		this.viewManager = viewManager;
		initUsers();
		init();
	}

	protected void init() {
		setSizeFull();
		setStyleName(Reindeer.PANEL_LIGHT);
		initUi();
		viewManager.getApplication().getMainWindow().removeAllActionHandlers();
		viewManager.getApplication().getMainWindow().addActionHandler(this);
	}
	
	protected void initUsers() {
		Map<String, User> userMap = viewManager.getApplication().getUserMap();
		userMap.clear();
		userMap.put("admin", new User()
			.setUsername("admin")
			.setPassword("admin")
			.setRole(User.ROLE_ADMIN));
		
		userMap.put("johndoe", new User()
			.setUsername("johndoe")
			.setPassword("reviewer")
			.setRole(User.ROLE_REVIEWER));
		
		userMap.put("janedoe", new User()
			.setUsername("janedoe")
			.setPassword("reviewer")
			.setRole(User.ROLE_REVIEWER));
		
		userMap.put("trademak", new User()
			.setUsername("trademak")
			.setPassword("author")
			.setRole(User.ROLE_USER));
		
		userMap.put("rvliempd", new User()
			.setUsername("rvliempd")
			.setPassword("author")
			.setRole(User.ROLE_USER));
	}

	protected void initUi() {
		initTitle();

		GridLayout layout = new GridLayout(2, 3);
		layout.setSpacing(true);
		addComponent(layout);

		initUsernameField(layout);
		initPasswordField(layout);
		initButtons(layout);
		usernameField.focus();
		getContent().setSizeUndefined();
	}

	protected void initTitle() {
		VerticalLayout verticalLayout = new VerticalLayout();
		titleLabel = new Label("Login");
		titleLabel.setStyleName(Reindeer.LABEL_H1);
		verticalLayout.addComponent(titleLabel);

		// add some empty space
		Label emptyLabel = new Label("");
		emptyLabel.setHeight("1.5em");
		verticalLayout.addComponent(emptyLabel);

		addComponent(verticalLayout);
	}

	protected void initUsernameField(GridLayout layout) {
		usernameField = new TextField();
		usernameField.setWidth("170px");
		usernameField.setWriteThrough(true);
		usernameField.setImmediate(true);
		usernameField.focus();

		layout.addComponent(new Label(USERNAME_FIELD));
		layout.addComponent(usernameField);
	}

	protected void initPasswordField(GridLayout layout) {
		passwordField = new PasswordField();
		passwordField.setWidth("170px");
		passwordField.setImmediate(true);
		passwordField.setDebugId("passwordTextField1");

		layout.addComponent(new Label(PASSWORD_FIELD));
		layout.addComponent(passwordField);
	}

	protected void initButtons(GridLayout layout) {
		final Button startButton = new Button("Login");
		startButton.setWidth("170px");
		startButton.addListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				login();
			}
		});

		HorizontalLayout footer = new HorizontalLayout();
		footer.setSpacing(true);
		footer.addComponent(startButton);
		layout.addComponent(new Label());
		layout.addComponent(footer);
	}

	protected void login() {
		Map<String, User> userMap = viewManager.getApplication().getUserMap();
		if(userMap.containsKey(usernameField.getValue().toString()) == false) {
			viewManager.showErrorMessage("Username is not known");
			viewManager.showLoginPage();
		} else if(userMap.get(usernameField.getValue().toString()).getPassword().equals(passwordField.getValue().toString()) == false) {
			viewManager.showErrorMessage("Password is not correct");
			viewManager.showLoginPage();
		} else {
			viewManager.showInfoMessage("Logged in");
			viewManager.getApplication().setUser(userMap.get(usernameField.getValue().toString()));
			viewManager.getApplication().getActionsPanel().enableButtons(true);
			viewManager.setInLoginPage(false);
			viewManager.showCasesPage();
		}
	}

	@Override
	public Action[] getActions(Object target, Object sender) {
		return new Action[] { enterKeyShortcutAction };
	}

	@Override
	public void handleAction(Action action, Object sender, Object target) {
		if (target instanceof PasswordField == true
				&& "passwordTextField1".equals(((PasswordField) target)
						.getDebugId())) {
			login();
		}
	}

}

