package com.developer.smallRoom.application.article.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ArticleServiceFactory {

    private final ArticleManagementService articleAdminManagementService;
    private final ArticleManagementService articleUserManagementService;

    @Autowired
    public ArticleServiceFactory(@Qualifier("ArticleAdminManagement") ArticleManagementService articleAdminManagementService,
                                 @Qualifier("ArticleUserManagement") ArticleManagementService articleUserManagementService) {
        this.articleAdminManagementService = articleAdminManagementService;
        this.articleUserManagementService = articleUserManagementService;
    }

    public ArticleManagementService getArticleManagementService(boolean isAdmin) {
        if (isAdmin) {
            return this.articleAdminManagementService;
        } else {
            return this.articleUserManagementService;
        }
    }
}
