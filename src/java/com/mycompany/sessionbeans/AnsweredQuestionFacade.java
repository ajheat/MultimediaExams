/*
 * Created by Aaron Newman on 2017.05.03  * 
 * Copyright © 2017 Aaron Newman. All rights reserved. * 
 */
package com.mycompany.sessionbeans;

import com.mycompany.entityclasses.AnsweredQuestion;
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
    
}
