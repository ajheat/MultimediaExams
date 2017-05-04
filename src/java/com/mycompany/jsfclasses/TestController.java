package com.mycompany.jsfclasses;

import com.mycompany.entityclasses.Question;
import com.mycompany.entityclasses.Test;
import com.mycompany.entityclasses.User;
import com.mycompany.jsfclasses.util.JsfUtil;
import com.mycompany.jsfclasses.util.JsfUtil.PersistAction;
import com.mycompany.managers.AccountManager;
import com.mycompany.sessionbeans.QuestionFacade;
import com.mycompany.sessionbeans.TestFacade;
import com.mycompany.sessionbeans.UserFacade;
import com.mycompany.sessionbeans.AnsweredQuestionFacade;

import java.io.IOException;

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
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

@Named("testController")
@SessionScoped
public class TestController implements Serializable {

    @EJB
    private com.mycompany.sessionbeans.TestFacade ejbFacade;
    @Inject
    private AccountManager accountManager;
    @EJB
    private UserFacade userFacade;
    @EJB
    private com.mycompany.sessionbeans.QuestionFacade questionFacade;
    private List<Test> items = null;
    private List<Test> items1 = null;
    private Test selected;

    private List<Question> testQuestions = null;

    public TestController() {
    }

    public Test getSelected() {
        return selected;
    }

    public void setSelected(Test selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private TestFacade getFacade() {
        return ejbFacade;
    }

    private QuestionFacade getQuestionFacade() {
        return questionFacade;
    }

    public Test prepareCreate() {
        selected = new Test();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("TestCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
            items1 = null;
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("TestUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("TestDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
            items1 = null;
        }
    }

    public void addQuestion() {
        if (selected != null) {
            selected.setNumQuestions(selected.getNumQuestions() + 1);
            update();
        }
    }

    public void removeQuestion() {
        if (selected != null) {
            selected.setNumQuestions(selected.getNumQuestions() - 1);
            update();
        }
    }

    public void changePoints(int oldVal, int newVal) {
        if (selected != null) {
            int delta = newVal - oldVal;
            selected.setTotalPoints(selected.getTotalPoints() + delta);
            update();
        }
    }

    public List<Test> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public List<Test> getItems1() {
        if (items1 == null) {
            items1 = getFacade().findOpenTests();
            System.out.println(items1.size());
        }
        return items1;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    if (persistAction == PersistAction.CREATE) {
                        selected.setNumQuestions(0);
                        selected.setTotalPoints(0);
                        selected.setOpen(false);
                        User u = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
                        selected.setUserId(u);
                    }
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

    public void studentQuestionSummary(ActionEvent actionEvent) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("active_test", selected);
        FacesContext.getCurrentInstance().getExternalContext().redirect("StudentQuestionView.xhtml");
    }

    public void grade(ActionEvent actionEvent) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("active_test", selected);
        FacesContext.getCurrentInstance().getExternalContext().redirect("TeacherGrader.xhtml");
    }

    public boolean mediaExsits() {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("mediaPath") != null;
    }

    public Test getTest(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Test> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Test> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public List<Question> getTestQuestions() {
        if (selected != null) {
            return getQuestionFacade().testQuery(selected);
        }
        return null;
    }

    public void questionSummary(ActionEvent actionEvent) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("active_test", selected);
        FacesContext.getCurrentInstance().getExternalContext().redirect("TestQuestions.xhtml");
    }

    @FacesConverter(forClass = Test.class)
    public static class TestControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TestController controller = (TestController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "testController");
            return controller.getTest(getKey(value));
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
            if (object instanceof Test) {
                Test o = (Test) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Test.class.getName()});
                return null;
            }
        }

    }

    public void assignGrade(ActionEvent actionEvent) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("active_test", selected);
        FacesContext.getCurrentInstance().getExternalContext().redirect("AssignGrade.xhtml");
    }

}
