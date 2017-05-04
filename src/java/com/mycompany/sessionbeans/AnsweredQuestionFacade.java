/*
 * Created by Aaron Newman on 2017.05.03  * 
 * Copyright © 2017 Aaron Newman. All rights reserved. * 
 */
package com.mycompany.sessionbeans;

import com.mycompany.entityclasses.AnsweredQuestion;
import com.mycompany.entityclasses.User;
import java.util.List;
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
    
    public AnsweredQuestion findByQuestionIdAndUser(Integer question, Integer user) {
        /*
        List<UserVideo> userVideo = em.createNamedQuery("UserVideo.findUserVideosByUserId")
                .setParameter("userId", userID)
                .getResultList();
        "SELECT u FROM UserVideo u WHERE u.userId.id = :userId"
        return userVideo;
        */
        
        if (em.createQuery("SELECT c FROM AnsweredQuestion c WHERE c.userId.id = :userId AND c.questionId.id = :questionId")
                .setParameter("userId", user)
                .setParameter("questionId", question)
                .getResultList().isEmpty()) {
            return null;
        } else {
            return (AnsweredQuestion) (em.createQuery("SELECT c FROM AnsweredQuestion c WHERE c.userId.id = :userId AND c.questionId.id = :questionId")
                    .setParameter("userId", user)
                    .setParameter("questionId", question)
                    .getResultList().get(0));
        }
    }
}
