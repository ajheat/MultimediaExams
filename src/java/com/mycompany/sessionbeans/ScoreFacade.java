/*
 * Created by Timothy Springsteen on 2017.05.04  * 
 * Copyright © 2017 Timothy Springsteen. All rights reserved. * 
 */
package com.mycompany.sessionbeans;

import com.mycompany.entityclasses.Score;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Timothy
 */
@Stateless
public class ScoreFacade extends AbstractFacade<Score> {

    @PersistenceContext(unitName = "MultimediaExamPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ScoreFacade() {
        super(Score.class);
    }
    
}
