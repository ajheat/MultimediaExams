package com.mycompany.jsfclasses;

import com.mycompany.entityclasses.AnsweredQuestion;
import com.mycompany.entityclasses.Question;
import com.mycompany.entityclasses.User;
import com.mycompany.jsfclasses.util.JsfUtil;
import com.mycompany.jsfclasses.util.JsfUtil.PersistAction;
import com.mycompany.managers.AccountManager;
import com.mycompany.sessionbeans.AnsweredQuestionFacade;
import com.mycompany.sessionbeans.UserFacade;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

@Named("answeredQuestionController")
@SessionScoped
public class AnsweredQuestionController implements Serializable {

    @EJB
    private com.mycompany.sessionbeans.AnsweredQuestionFacade ejbFacade;
    private List<AnsweredQuestion> items = null;
    private AnsweredQuestion selected;
    
    @EJB
    private UserFacade userFacade;
    
    @Inject
    private QuestionController questionController;
    @Inject
    private AccountManager accountManager;

    public AnsweredQuestionController() {
    }

    public AnsweredQuestion getSelected() {
        return selected;
    }

    public void setSelected(AnsweredQuestion selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private AnsweredQuestionFacade getFacade() {
        return ejbFacade;
    }

    public AnsweredQuestion prepareCreate() {
        selected = new AnsweredQuestion();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("AnsweredQuestionCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }
    
    public int isAnswered(Integer id) {
        User user = userFacade.getUser(accountManager.getUserID());
            System.out.println(id);
              
        /*
        String questionID = item.getId() + "";
        String userID = user.getId() + "";
        System.out.println(questionID);
        AnsweredQuestion answeredQuestion = getFacade().findByQuestionIdAndUser(questionID, userID);
        if (answeredQuestion == null)
            return false;
            */
        return 1;
    }
    
    public void answer() {
        Question selected = questionController.getSelected();
        User user = userFacade.getUser(accountManager.getUserID());
        AnsweredQuestion newAnsweredQuestion = new AnsweredQuestion("hi", selected.getPoints(), user, selected);
        
        ejbFacade.create(newAnsweredQuestion);
        //initializeEmbeddableKey();
        //persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("AnsweredVideoCreated"));
        //if (!JsfUtil.isValidationFailed()) {
        //    items = null;    // Invalidate list of items to trigger re-query.
        //}
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("AnsweredQuestionUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("AnsweredQuestionDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<AnsweredQuestion> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public AnsweredQuestion getAnsweredQuestion(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<AnsweredQuestion> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<AnsweredQuestion> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = AnsweredQuestion.class)
    public static class AnsweredQuestionControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AnsweredQuestionController controller = (AnsweredQuestionController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "answeredQuestionController");
            return controller.getAnsweredQuestion(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof AnsweredQuestion) {
                AnsweredQuestion o = (AnsweredQuestion) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), AnsweredQuestion.class.getName()});
                return null;
            }
        }

    }

}
