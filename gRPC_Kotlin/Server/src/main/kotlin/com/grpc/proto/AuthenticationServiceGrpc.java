package com.grpc.proto;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.60.0)",
    comments = "Source: authentication.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class AuthenticationServiceGrpc {

  private AuthenticationServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "AuthenticationService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.grpc.proto.Authentication.RegistrationRequest,
      com.grpc.proto.Authentication.RegistrationResponse> getRegisterUserMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RegisterUser",
      requestType = com.grpc.proto.Authentication.RegistrationRequest.class,
      responseType = com.grpc.proto.Authentication.RegistrationResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.grpc.proto.Authentication.RegistrationRequest,
      com.grpc.proto.Authentication.RegistrationResponse> getRegisterUserMethod() {
    io.grpc.MethodDescriptor<com.grpc.proto.Authentication.RegistrationRequest, com.grpc.proto.Authentication.RegistrationResponse> getRegisterUserMethod;
    if ((getRegisterUserMethod = AuthenticationServiceGrpc.getRegisterUserMethod) == null) {
      synchronized (AuthenticationServiceGrpc.class) {
        if ((getRegisterUserMethod = AuthenticationServiceGrpc.getRegisterUserMethod) == null) {
          AuthenticationServiceGrpc.getRegisterUserMethod = getRegisterUserMethod =
              io.grpc.MethodDescriptor.<com.grpc.proto.Authentication.RegistrationRequest, com.grpc.proto.Authentication.RegistrationResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "RegisterUser"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.grpc.proto.Authentication.RegistrationRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.grpc.proto.Authentication.RegistrationResponse.getDefaultInstance()))
              .setSchemaDescriptor(new AuthenticationServiceMethodDescriptorSupplier("RegisterUser"))
              .build();
        }
      }
    }
    return getRegisterUserMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.grpc.proto.Authentication.AuthenticationRequest,
      com.grpc.proto.Authentication.Token> getCreateTokenMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateToken",
      requestType = com.grpc.proto.Authentication.AuthenticationRequest.class,
      responseType = com.grpc.proto.Authentication.Token.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.grpc.proto.Authentication.AuthenticationRequest,
      com.grpc.proto.Authentication.Token> getCreateTokenMethod() {
    io.grpc.MethodDescriptor<com.grpc.proto.Authentication.AuthenticationRequest, com.grpc.proto.Authentication.Token> getCreateTokenMethod;
    if ((getCreateTokenMethod = AuthenticationServiceGrpc.getCreateTokenMethod) == null) {
      synchronized (AuthenticationServiceGrpc.class) {
        if ((getCreateTokenMethod = AuthenticationServiceGrpc.getCreateTokenMethod) == null) {
          AuthenticationServiceGrpc.getCreateTokenMethod = getCreateTokenMethod =
              io.grpc.MethodDescriptor.<com.grpc.proto.Authentication.AuthenticationRequest, com.grpc.proto.Authentication.Token>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreateToken"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.grpc.proto.Authentication.AuthenticationRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.grpc.proto.Authentication.Token.getDefaultInstance()))
              .setSchemaDescriptor(new AuthenticationServiceMethodDescriptorSupplier("CreateToken"))
              .build();
        }
      }
    }
    return getCreateTokenMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.grpc.proto.Authentication.Token,
      com.grpc.proto.Authentication.BoolValue> getVerifyTokenMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "VerifyToken",
      requestType = com.grpc.proto.Authentication.Token.class,
      responseType = com.grpc.proto.Authentication.BoolValue.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.grpc.proto.Authentication.Token,
      com.grpc.proto.Authentication.BoolValue> getVerifyTokenMethod() {
    io.grpc.MethodDescriptor<com.grpc.proto.Authentication.Token, com.grpc.proto.Authentication.BoolValue> getVerifyTokenMethod;
    if ((getVerifyTokenMethod = AuthenticationServiceGrpc.getVerifyTokenMethod) == null) {
      synchronized (AuthenticationServiceGrpc.class) {
        if ((getVerifyTokenMethod = AuthenticationServiceGrpc.getVerifyTokenMethod) == null) {
          AuthenticationServiceGrpc.getVerifyTokenMethod = getVerifyTokenMethod =
              io.grpc.MethodDescriptor.<com.grpc.proto.Authentication.Token, com.grpc.proto.Authentication.BoolValue>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "VerifyToken"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.grpc.proto.Authentication.Token.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.grpc.proto.Authentication.BoolValue.getDefaultInstance()))
              .setSchemaDescriptor(new AuthenticationServiceMethodDescriptorSupplier("VerifyToken"))
              .build();
        }
      }
    }
    return getVerifyTokenMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.grpc.proto.Authentication.Token,
      com.grpc.proto.Authentication.BoolValue> getDestroyTokenMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DestroyToken",
      requestType = com.grpc.proto.Authentication.Token.class,
      responseType = com.grpc.proto.Authentication.BoolValue.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.grpc.proto.Authentication.Token,
      com.grpc.proto.Authentication.BoolValue> getDestroyTokenMethod() {
    io.grpc.MethodDescriptor<com.grpc.proto.Authentication.Token, com.grpc.proto.Authentication.BoolValue> getDestroyTokenMethod;
    if ((getDestroyTokenMethod = AuthenticationServiceGrpc.getDestroyTokenMethod) == null) {
      synchronized (AuthenticationServiceGrpc.class) {
        if ((getDestroyTokenMethod = AuthenticationServiceGrpc.getDestroyTokenMethod) == null) {
          AuthenticationServiceGrpc.getDestroyTokenMethod = getDestroyTokenMethod =
              io.grpc.MethodDescriptor.<com.grpc.proto.Authentication.Token, com.grpc.proto.Authentication.BoolValue>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DestroyToken"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.grpc.proto.Authentication.Token.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.grpc.proto.Authentication.BoolValue.getDefaultInstance()))
              .setSchemaDescriptor(new AuthenticationServiceMethodDescriptorSupplier("DestroyToken"))
              .build();
        }
      }
    }
    return getDestroyTokenMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.grpc.proto.Authentication.UpdateRequest,
      com.grpc.proto.Authentication.UpdateResponse> getUpdateUserMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UpdateUser",
      requestType = com.grpc.proto.Authentication.UpdateRequest.class,
      responseType = com.grpc.proto.Authentication.UpdateResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.grpc.proto.Authentication.UpdateRequest,
      com.grpc.proto.Authentication.UpdateResponse> getUpdateUserMethod() {
    io.grpc.MethodDescriptor<com.grpc.proto.Authentication.UpdateRequest, com.grpc.proto.Authentication.UpdateResponse> getUpdateUserMethod;
    if ((getUpdateUserMethod = AuthenticationServiceGrpc.getUpdateUserMethod) == null) {
      synchronized (AuthenticationServiceGrpc.class) {
        if ((getUpdateUserMethod = AuthenticationServiceGrpc.getUpdateUserMethod) == null) {
          AuthenticationServiceGrpc.getUpdateUserMethod = getUpdateUserMethod =
              io.grpc.MethodDescriptor.<com.grpc.proto.Authentication.UpdateRequest, com.grpc.proto.Authentication.UpdateResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UpdateUser"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.grpc.proto.Authentication.UpdateRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.grpc.proto.Authentication.UpdateResponse.getDefaultInstance()))
              .setSchemaDescriptor(new AuthenticationServiceMethodDescriptorSupplier("UpdateUser"))
              .build();
        }
      }
    }
    return getUpdateUserMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.grpc.proto.Authentication.DeleteRequest,
      com.grpc.proto.Authentication.DeleteResponse> getDeleteUserMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DeleteUser",
      requestType = com.grpc.proto.Authentication.DeleteRequest.class,
      responseType = com.grpc.proto.Authentication.DeleteResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.grpc.proto.Authentication.DeleteRequest,
      com.grpc.proto.Authentication.DeleteResponse> getDeleteUserMethod() {
    io.grpc.MethodDescriptor<com.grpc.proto.Authentication.DeleteRequest, com.grpc.proto.Authentication.DeleteResponse> getDeleteUserMethod;
    if ((getDeleteUserMethod = AuthenticationServiceGrpc.getDeleteUserMethod) == null) {
      synchronized (AuthenticationServiceGrpc.class) {
        if ((getDeleteUserMethod = AuthenticationServiceGrpc.getDeleteUserMethod) == null) {
          AuthenticationServiceGrpc.getDeleteUserMethod = getDeleteUserMethod =
              io.grpc.MethodDescriptor.<com.grpc.proto.Authentication.DeleteRequest, com.grpc.proto.Authentication.DeleteResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DeleteUser"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.grpc.proto.Authentication.DeleteRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.grpc.proto.Authentication.DeleteResponse.getDefaultInstance()))
              .setSchemaDescriptor(new AuthenticationServiceMethodDescriptorSupplier("DeleteUser"))
              .build();
        }
      }
    }
    return getDeleteUserMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static AuthenticationServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AuthenticationServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AuthenticationServiceStub>() {
        @java.lang.Override
        public AuthenticationServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AuthenticationServiceStub(channel, callOptions);
        }
      };
    return AuthenticationServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static AuthenticationServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AuthenticationServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AuthenticationServiceBlockingStub>() {
        @java.lang.Override
        public AuthenticationServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AuthenticationServiceBlockingStub(channel, callOptions);
        }
      };
    return AuthenticationServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static AuthenticationServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AuthenticationServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AuthenticationServiceFutureStub>() {
        @java.lang.Override
        public AuthenticationServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AuthenticationServiceFutureStub(channel, callOptions);
        }
      };
    return AuthenticationServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void registerUser(com.grpc.proto.Authentication.RegistrationRequest request,
        io.grpc.stub.StreamObserver<com.grpc.proto.Authentication.RegistrationResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRegisterUserMethod(), responseObserver);
    }

    /**
     */
    default void createToken(com.grpc.proto.Authentication.AuthenticationRequest request,
        io.grpc.stub.StreamObserver<com.grpc.proto.Authentication.Token> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCreateTokenMethod(), responseObserver);
    }

    /**
     */
    default void verifyToken(com.grpc.proto.Authentication.Token request,
        io.grpc.stub.StreamObserver<com.grpc.proto.Authentication.BoolValue> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getVerifyTokenMethod(), responseObserver);
    }

    /**
     */
    default void destroyToken(com.grpc.proto.Authentication.Token request,
        io.grpc.stub.StreamObserver<com.grpc.proto.Authentication.BoolValue> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDestroyTokenMethod(), responseObserver);
    }

    /**
     */
    default void updateUser(com.grpc.proto.Authentication.UpdateRequest request,
        io.grpc.stub.StreamObserver<com.grpc.proto.Authentication.UpdateResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUpdateUserMethod(), responseObserver);
    }

    /**
     */
    default void deleteUser(com.grpc.proto.Authentication.DeleteRequest request,
        io.grpc.stub.StreamObserver<com.grpc.proto.Authentication.DeleteResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDeleteUserMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service AuthenticationService.
   */
  public static abstract class AuthenticationServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return AuthenticationServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service AuthenticationService.
   */
  public static final class AuthenticationServiceStub
      extends io.grpc.stub.AbstractAsyncStub<AuthenticationServiceStub> {
    private AuthenticationServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AuthenticationServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AuthenticationServiceStub(channel, callOptions);
    }

    /**
     */
    public void registerUser(com.grpc.proto.Authentication.RegistrationRequest request,
        io.grpc.stub.StreamObserver<com.grpc.proto.Authentication.RegistrationResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRegisterUserMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void createToken(com.grpc.proto.Authentication.AuthenticationRequest request,
        io.grpc.stub.StreamObserver<com.grpc.proto.Authentication.Token> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreateTokenMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void verifyToken(com.grpc.proto.Authentication.Token request,
        io.grpc.stub.StreamObserver<com.grpc.proto.Authentication.BoolValue> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getVerifyTokenMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void destroyToken(com.grpc.proto.Authentication.Token request,
        io.grpc.stub.StreamObserver<com.grpc.proto.Authentication.BoolValue> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDestroyTokenMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void updateUser(com.grpc.proto.Authentication.UpdateRequest request,
        io.grpc.stub.StreamObserver<com.grpc.proto.Authentication.UpdateResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUpdateUserMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void deleteUser(com.grpc.proto.Authentication.DeleteRequest request,
        io.grpc.stub.StreamObserver<com.grpc.proto.Authentication.DeleteResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDeleteUserMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service AuthenticationService.
   */
  public static final class AuthenticationServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<AuthenticationServiceBlockingStub> {
    private AuthenticationServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AuthenticationServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AuthenticationServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.grpc.proto.Authentication.RegistrationResponse registerUser(com.grpc.proto.Authentication.RegistrationRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRegisterUserMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.grpc.proto.Authentication.Token createToken(com.grpc.proto.Authentication.AuthenticationRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateTokenMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.grpc.proto.Authentication.BoolValue verifyToken(com.grpc.proto.Authentication.Token request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getVerifyTokenMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.grpc.proto.Authentication.BoolValue destroyToken(com.grpc.proto.Authentication.Token request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDestroyTokenMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.grpc.proto.Authentication.UpdateResponse updateUser(com.grpc.proto.Authentication.UpdateRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdateUserMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.grpc.proto.Authentication.DeleteResponse deleteUser(com.grpc.proto.Authentication.DeleteRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeleteUserMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service AuthenticationService.
   */
  public static final class AuthenticationServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<AuthenticationServiceFutureStub> {
    private AuthenticationServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AuthenticationServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AuthenticationServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.grpc.proto.Authentication.RegistrationResponse> registerUser(
        com.grpc.proto.Authentication.RegistrationRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRegisterUserMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.grpc.proto.Authentication.Token> createToken(
        com.grpc.proto.Authentication.AuthenticationRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreateTokenMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.grpc.proto.Authentication.BoolValue> verifyToken(
        com.grpc.proto.Authentication.Token request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getVerifyTokenMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.grpc.proto.Authentication.BoolValue> destroyToken(
        com.grpc.proto.Authentication.Token request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDestroyTokenMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.grpc.proto.Authentication.UpdateResponse> updateUser(
        com.grpc.proto.Authentication.UpdateRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUpdateUserMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.grpc.proto.Authentication.DeleteResponse> deleteUser(
        com.grpc.proto.Authentication.DeleteRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDeleteUserMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_REGISTER_USER = 0;
  private static final int METHODID_CREATE_TOKEN = 1;
  private static final int METHODID_VERIFY_TOKEN = 2;
  private static final int METHODID_DESTROY_TOKEN = 3;
  private static final int METHODID_UPDATE_USER = 4;
  private static final int METHODID_DELETE_USER = 5;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_REGISTER_USER:
          serviceImpl.registerUser((com.grpc.proto.Authentication.RegistrationRequest) request,
              (io.grpc.stub.StreamObserver<com.grpc.proto.Authentication.RegistrationResponse>) responseObserver);
          break;
        case METHODID_CREATE_TOKEN:
          serviceImpl.createToken((com.grpc.proto.Authentication.AuthenticationRequest) request,
              (io.grpc.stub.StreamObserver<com.grpc.proto.Authentication.Token>) responseObserver);
          break;
        case METHODID_VERIFY_TOKEN:
          serviceImpl.verifyToken((com.grpc.proto.Authentication.Token) request,
              (io.grpc.stub.StreamObserver<com.grpc.proto.Authentication.BoolValue>) responseObserver);
          break;
        case METHODID_DESTROY_TOKEN:
          serviceImpl.destroyToken((com.grpc.proto.Authentication.Token) request,
              (io.grpc.stub.StreamObserver<com.grpc.proto.Authentication.BoolValue>) responseObserver);
          break;
        case METHODID_UPDATE_USER:
          serviceImpl.updateUser((com.grpc.proto.Authentication.UpdateRequest) request,
              (io.grpc.stub.StreamObserver<com.grpc.proto.Authentication.UpdateResponse>) responseObserver);
          break;
        case METHODID_DELETE_USER:
          serviceImpl.deleteUser((com.grpc.proto.Authentication.DeleteRequest) request,
              (io.grpc.stub.StreamObserver<com.grpc.proto.Authentication.DeleteResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getRegisterUserMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.grpc.proto.Authentication.RegistrationRequest,
              com.grpc.proto.Authentication.RegistrationResponse>(
                service, METHODID_REGISTER_USER)))
        .addMethod(
          getCreateTokenMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.grpc.proto.Authentication.AuthenticationRequest,
              com.grpc.proto.Authentication.Token>(
                service, METHODID_CREATE_TOKEN)))
        .addMethod(
          getVerifyTokenMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.grpc.proto.Authentication.Token,
              com.grpc.proto.Authentication.BoolValue>(
                service, METHODID_VERIFY_TOKEN)))
        .addMethod(
          getDestroyTokenMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.grpc.proto.Authentication.Token,
              com.grpc.proto.Authentication.BoolValue>(
                service, METHODID_DESTROY_TOKEN)))
        .addMethod(
          getUpdateUserMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.grpc.proto.Authentication.UpdateRequest,
              com.grpc.proto.Authentication.UpdateResponse>(
                service, METHODID_UPDATE_USER)))
        .addMethod(
          getDeleteUserMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.grpc.proto.Authentication.DeleteRequest,
              com.grpc.proto.Authentication.DeleteResponse>(
                service, METHODID_DELETE_USER)))
        .build();
  }

  private static abstract class AuthenticationServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    AuthenticationServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.grpc.proto.Authentication.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("AuthenticationService");
    }
  }

  private static final class AuthenticationServiceFileDescriptorSupplier
      extends AuthenticationServiceBaseDescriptorSupplier {
    AuthenticationServiceFileDescriptorSupplier() {}
  }

  private static final class AuthenticationServiceMethodDescriptorSupplier
      extends AuthenticationServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    AuthenticationServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (AuthenticationServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new AuthenticationServiceFileDescriptorSupplier())
              .addMethod(getRegisterUserMethod())
              .addMethod(getCreateTokenMethod())
              .addMethod(getVerifyTokenMethod())
              .addMethod(getDestroyTokenMethod())
              .addMethod(getUpdateUserMethod())
              .addMethod(getDeleteUserMethod())
              .build();
        }
      }
    }
    return result;
  }
}
