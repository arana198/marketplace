module com.marketplace {

     requires static lombok;

     requires java.annotation;
     requires java.persistence;
     requires java.validation;
     requires javax.transaction.api;

     requires spring.web;
     requires spring.webmvc;
     requires spring.data.jpa;
     requires spring.security.config;
     requires spring.tx;
     requires spring.integration.core;
     requires spring.core;
     requires spring.orm;
     requires spring.hateoas;
     requires spring.boot;
     requires spring.beans;
     requires spring.security.web;
     requires spring.social.config;
     requires spring.social.core;
     requires spring.social.security;
     requires spring.social.google;
     requires spring.data.commons;
     requires spring.context;
     requires spring.security.core;
     requires spring.amqp;
     requires spring.messaging;
     requires spring.integration.amqp;
     requires spring.rabbit;
     requires spring.boot.autoconfigure;
     requires spring.security.oauth2;

     requires tomcat.embed.core;
     requires jackson.annotations;
     requires hibernate.envers;

     requires com.fasterxml.jackson.core;
     requires com.fasterxml.jackson.databind;

     requires jts.core;
     requires gson;

     exports com.marketplace.user.service;
     exports com.marketplace.storage.service;
     exports com.marketplace.profile.service;
     exports com.marketplace.notification;
     exports com.marketplace.location.service;
     exports com.marketplace.company.service;
}