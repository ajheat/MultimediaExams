/*
 * Created by Aaron Newman on 2017.05.03  * 
 * Copyright © 2017 Aaron Newman. All rights reserved. * 
 */
package com.mycompany.sessionbeans;

import com.mycompany.entityclasses.AnsweredQuestion;
import com.mycompany.entityclasses.User;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Aaron
 */
@Stateless
public class AnsweredQuestionFacade extends AbstractFacade<AnsweredQuestion> {

    @PersistenceContext(unitName = "MultimediaExamPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AnsweredQuestionFacade() {
        super(AnsweredQuestion.class);
    }
    
    public AnsweredQuestion findByQuestionIdAndUser(String question, String user) {
        if (em.createQuery("SELECT c FROM AnsweredQuestion c WHERE c.user_id = :user AND c.question_id = :question")
                .setParameter("user", user)
                .setParameter("question", question)
                .getResultList().isEmpty()) {
            return null;
        } else {
            return (AnsweredQuestion) (em.createQuery("SELECT c FROM AnsweredQuestion c WHERE c.user_id = :user AND c.question_id = :question")
                    .setParameter("user", user)
                    .setParameter("question", question)
                    .getSingleResult());
        }
    }
}
