/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sessionbeans;

import com.mycompany.entityclasses.AnsweredQuestion;
import com.mycompany.entityclasses.Question;
import com.mycompany.entityclasses.Test;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Andrew
 */
@Stateless
public class QuestionFacade extends AbstractFacade<Question> {

    @PersistenceContext(unitName = "MultimediaExamPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public QuestionFacade() {
        super(Question.class);
    }
    
    public List<Question> testQuery(Test testId) {
        return getEntityManager().createQuery("SELECT q FROM Question q WHERE q.testId=:testId").setParameter("testId", testId).getResultList();
    }
    
    public List<Question> findByTestId(Integer test) {
        /*
        List<UserVideo> userVideo = em.createNamedQuery("UserVideo.findUserVideosByUserId")
                .setParameter("userId", userID)
                .getResultList();
        "SELECT u FROM UserVideo u WHERE u.userId.id = :userId"
        return userVideo;
        */
        
        if (em.createQuery("SELECT c FROM AnsweredQuestion c WHERE c.testId.id = :testId")
                .setParameter("testId", test)
                .getResultList().isEmpty()) {
            return null;
        } else {
            return (em.createQuery("SELECT c FROM AnsweredQuestion c WHERE c.testId.id = :testId")
                    .setParameter("testId", test)
                    .getResultList());
        }
    }
}
