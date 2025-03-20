package com.megacityCab.megaCityCabBackEnd.entity.custom;

import java.util.Date;

public interface CommentCustomResult {
    long getCommentId();
    long getUserId();
    String getUserName();
    String getComment();
    Date getDate();
    String getStatus();


}
