/*
 * Created by Aaron Newman on 2017.05.03  * 
 * Copyright © 2017 Aaron Newman. All rights reserved. * 
 */
package com.mycompany.entityclasses;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Aaron
 */
@Entity
@Table(name = "AnsweredQuestion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AnsweredQuestion.findAll", query = "SELECT a FROM AnsweredQuestion a")
    , @NamedQuery(name = "AnsweredQuestion.findById", query = "SELECT a FROM AnsweredQuestion a WHERE a.id = :id")
    , @NamedQuery(name = "AnsweredQuestion.findByPoints", query = "SELECT a FROM AnsweredQuestion a WHERE a.points = :points")
    , @NamedQuery(name = "AnsweredQuestion.findByComment", query = "SELECT a FROM AnsweredQuestion a WHERE a.comment = :comment")})
public class AnsweredQuestion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "answer")
    private String answer;
    @Column(name = "points")
    private Integer points;
    @Size(max = 2048)
    @Column(name = "comment")
    private String comment;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User userId;
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Question questionId;

    public AnsweredQuestion() {
    }

    public AnsweredQuestion(Integer id) {
        this.id = id;
    }

    public AnsweredQuestion(String answer, Integer points, User userId,
            Question questionId) {
        
        this.answer = answer;
        this.points = points;
        this.userId = userId;
        this.questionId = questionId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public Question getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Question questionId) {
        this.questionId = questionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AnsweredQuestion)) {
            return false;
        }
        AnsweredQuestion other = (AnsweredQuestion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.entityclasses.AnsweredQuestion[ id=" + id + " ]";
    }
    
}
