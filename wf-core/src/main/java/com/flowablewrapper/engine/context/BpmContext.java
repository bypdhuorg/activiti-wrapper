package com.flowablewrapper.engine.context;

import com.flowablewrapper.bean.db.BpmDefinition;
import com.flowablewrapper.engine.action.cmd.IActionCmd;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class BpmContext {

    private static ThreadLocal<Stack<IActionCmd>> threadActionModel = new ThreadLocal();
    private static ThreadLocal<Map<Long, BpmDefinition>> threadBpmProcessDef = new ThreadLocal();

    public BpmContext() {
    }

    public static void setActionModel(IActionCmd actionModel) {
        getStack(threadActionModel).push(actionModel);
    }

    public static IActionCmd getActionModel() {
        Stack<IActionCmd> stack = getStack(threadActionModel);
        return stack.isEmpty() ? null : (IActionCmd)stack.peek();
    }

    public static IActionCmd submitActionModel() {
        Stack<IActionCmd> stack = getStack(threadActionModel);
        return stack.isEmpty() ? null : (IActionCmd)stack.firstElement();
    }

    public static void removeActionModel() {
        Stack stack = getStack(threadActionModel);
        if (!stack.isEmpty()) {
            stack.pop();
        }

    }

    public static BpmDefinition getProcessDef(Long defId) {
        Map<Long, BpmDefinition> map = getThreadMap(threadBpmProcessDef);
        return (BpmDefinition)map.get(defId);
    }

    public static void addProcessDef(Long defId, BpmDefinition processDef) {
        getThreadMap(threadBpmProcessDef).put(defId, processDef);
    }

    public static void cleanTread() {
        threadActionModel.remove();
        threadBpmProcessDef.remove();
    }

    protected static <T> Stack<T> getStack(ThreadLocal<Stack<T>> threadLocal) {
        Stack<T> stack = (Stack)threadLocal.get();
        if (stack == null) {
            stack = new Stack();
            threadLocal.set(stack);
        }

        return stack;
    }

    protected static <T> Map<Long, T> getThreadMap(ThreadLocal<Map<Long, T>> threadMap) {
        Map<Long, T> processDefMap = (Map)threadMap.get();
        if (processDefMap == null) {
            processDefMap = new HashMap();
            threadMap.set(processDefMap);
        }

        return (Map)processDefMap;
    }
}
