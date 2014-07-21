package be.xhibit.teletask.model.spec.function;

public interface FunctionExecution<C extends FunctionExecutionContext, R extends FunctionExecutionPayload> {
    R createPayload(C context);
}
