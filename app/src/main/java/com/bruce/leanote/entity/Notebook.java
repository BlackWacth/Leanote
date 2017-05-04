package com.bruce.leanote.entity;

/**
 * Created by Bruce on 2017/5/4.
 */

public class Notebook {


    /**
     * NotebookId : 585f767dab64417326002874
     * UserId : 585f767dab64417326002873
     * ParentNotebookId :
     * Seq : -1
     * Title : life
     * UrlTitle : life
     * IsBlog : false
     * CreatedTime : 2016-12-25T15:34:21.239+08:00
     * UpdatedTime : 2016-12-25T15:34:21.239+08:00
     * Usn : 7
     * IsDeleted : true
     */
    private String NotebookId;
    private String UserId;
    private String ParentNotebookId;
    private int Seq;
    private String Title;
    private String UrlTitle;
    private boolean IsBlog;
    private String CreatedTime;
    private String UpdatedTime;
    private int Usn;
    private boolean IsDeleted;

    public String getNotebookId() {
        return NotebookId;
    }

    public void setNotebookId(String NotebookId) {
        this.NotebookId = NotebookId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public String getParentNotebookId() {
        return ParentNotebookId;
    }

    public void setParentNotebookId(String ParentNotebookId) {
        this.ParentNotebookId = ParentNotebookId;
    }

    public int getSeq() {
        return Seq;
    }

    public void setSeq(int Seq) {
        this.Seq = Seq;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getUrlTitle() {
        return UrlTitle;
    }

    public void setUrlTitle(String UrlTitle) {
        this.UrlTitle = UrlTitle;
    }

    public boolean isIsBlog() {
        return IsBlog;
    }

    public void setIsBlog(boolean IsBlog) {
        this.IsBlog = IsBlog;
    }

    public String getCreatedTime() {
        return CreatedTime;
    }

    public void setCreatedTime(String CreatedTime) {
        this.CreatedTime = CreatedTime;
    }

    public String getUpdatedTime() {
        return UpdatedTime;
    }

    public void setUpdatedTime(String UpdatedTime) {
        this.UpdatedTime = UpdatedTime;
    }

    public int getUsn() {
        return Usn;
    }

    public void setUsn(int Usn) {
        this.Usn = Usn;
    }

    public boolean isIsDeleted() {
        return IsDeleted;
    }

    public void setIsDeleted(boolean IsDeleted) {
        this.IsDeleted = IsDeleted;
    }

    @Override
    public String toString() {
        return "Notebook{" +
                "NotebookId='" + NotebookId + '\'' +
                ", UserId='" + UserId + '\'' +
                ", ParentNotebookId='" + ParentNotebookId + '\'' +
                ", Seq=" + Seq +
                ", Title='" + Title + '\'' +
                ", UrlTitle='" + UrlTitle + '\'' +
                ", IsBlog=" + IsBlog +
                ", CreatedTime='" + CreatedTime + '\'' +
                ", UpdatedTime='" + UpdatedTime + '\'' +
                ", Usn=" + Usn +
                ", IsDeleted=" + IsDeleted +
                '}';
    }
}
