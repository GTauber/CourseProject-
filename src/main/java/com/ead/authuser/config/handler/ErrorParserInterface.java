package com.ead.authuser.config.handler;

import com.ead.authuser.models.entity.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.reactivestreams.Publisher;

@FunctionalInterface
public interface ErrorParserInterface {

    Publisher<Void> parse(Response<?> error) throws JsonProcessingException;

}
