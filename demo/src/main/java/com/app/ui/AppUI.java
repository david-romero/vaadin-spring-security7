/**
 * AppUi.java
 * appEducacionalVaadin
 * 29/11/2014 14:39:22
 * Copyright David
 * com.app.ui
 */
package com.app.ui;

import java.util.Collection;

import javax.servlet.annotation.WebListener;
import javax.servlet.annotation.WebServlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.ContextLoaderListener;

import com.app.infrastructure.security.Authority;
import com.app.infrastructure.security.UserAccount;
import com.app.presenter.event.EventComunicationBus;
import com.app.ui.login.view.LoginView;
import com.app.ui.user.admin.view.AdminView;
import com.app.ui.user.usuario.view.UsuarioView;
import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.vaadin.annotations.Theme;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.ErrorHandler;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.EnableVaadin;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.spring.server.SpringVaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SpringUI
@Theme("appeducacionalvaadin")
/**
 * @author David
 *
 */
public class AppUI extends UI implements ErrorHandler{
	
	
	@WebServlet(value = "/*", asyncSupported = true)
    public static class Servlet extends SpringVaadinServlet {

		/**
		 * 
		 */
		private static final long serialVersionUID = 8438556552964111310L;
    }

    @WebListener
    public static class CustomLoaderListener extends ContextLoaderListener {
    }

    @Configuration
    @EnableVaadin
    public static class CustomConfiguration {
    	
    	
    }

	/**
	 * 
	 */
	private static final long serialVersionUID = -2252711826491740298L;
	
	@Autowired SpringViewProvider springViewProvider;
	
	/**
	 * 
	 */
	@Autowired
    private transient ApplicationContext applicationContext;
	
	
	private final EventComunicationBus eventComunicationBus = new EventComunicationBus();

	@Override
	protected void init(VaadinRequest request) {
		VaadinSession.getCurrent().setErrorHandler(this);
		Responsive.makeResponsive(this);
		EventComunicationBus.register(this);
		NavigatorUI navigator = new NavigatorUI(this, this);
		

		navigator.addProvider(springViewProvider);
		
		navigator.addViewChangeListener(new ViewChangeListenerUI());
		navigator.addView("login", LoginView.class);

		navigator.addView(Authority.ADMINISTRADOR, AdminView.class);
		
		navigator.addView(Authority.USUARIO, UsuarioView.class);
		
		setNavigator(navigator);

		navigator.navigateTo("login");
		
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
	public SpringViewProvider getSpringViewProvider() {
		return springViewProvider;
	}
	
	public static EventComunicationBus getEventComunicationBus() {
        return ((AppUI) getCurrent()).eventComunicationBus;
    }
    
    public static String getAuthority(){
    	SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		String authority = "";
		if (authentication != null && authentication.isAuthenticated()) {
			Collection<? extends GrantedAuthority> authorities = authentication
					.getAuthorities();
			GrantedAuthority grantedAuthority = Lists.newArrayList(authorities)
					.get(0);
			authority = grantedAuthority.getAuthority();
		}
		return authority;
    }
    
    public static UserAccount getCurrentUser() {
        return (UserAccount) VaadinSession.getCurrent().getAttribute(
                UserAccount.class);
    }
    
    

	/* (non-Javadoc)
	 * @see com.vaadin.server.ErrorHandler#error(com.vaadin.server.ErrorEvent)
	 */
	@Override
	public void error(com.vaadin.server.ErrorEvent event) {
		 // connector event
        if (event.getThrowable().getCause() instanceof AccessDeniedException)
        {
            AccessDeniedException accessDeniedException = (AccessDeniedException) event.getThrowable().getCause();
            Notification.show(accessDeniedException.getMessage(), Notification.Type.ERROR_MESSAGE);

            // Cleanup view. Now Vaadin ignores errors and always shows the view.  :-(
            // since beta10
            setContent(null);
            return;
        }

        // Error on page load. Now it doesn't work. User sees standard error page.
        if (event.getThrowable() instanceof AccessDeniedException)
        {
            AccessDeniedException exception = (AccessDeniedException) event.getThrowable();

            Label label = new Label(exception.getMessage());
            label.setWidth(-1, Unit.PERCENTAGE);

            Link goToMain = new Link("Go to main", new ExternalResource("/"));

            VerticalLayout layout = new VerticalLayout();
            layout.addComponent(label);
            layout.addComponent(goToMain);
            layout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
            layout.setComponentAlignment(goToMain, Alignment.MIDDLE_CENTER);

            VerticalLayout mainLayout = new VerticalLayout();
            mainLayout.setSizeFull();
            mainLayout.addComponent(layout);
            mainLayout.setComponentAlignment(layout, Alignment.MIDDLE_CENTER);

            setContent(mainLayout);
            Notification.show(exception.getMessage(), Notification.Type.ERROR_MESSAGE);
            return;
        }

        DefaultErrorHandler.doDefault(event);
	}
}
