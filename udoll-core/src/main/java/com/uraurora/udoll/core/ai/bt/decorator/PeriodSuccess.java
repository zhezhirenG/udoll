package com.uraurora.udoll.core.ai.bt.decorator;

import com.uraurora.udoll.core.ai.bt.INode;
import com.uraurora.udoll.core.ai.bt.LoopDecoratorNode;
import com.uraurora.udoll.core.ai.bt.annotation.NodeAttribute;
import com.uraurora.udoll.core.ai.bt.value.NodeContext;

import java.time.LocalDateTime;

/**
 * @author : gaoxiaodong04
 * @program : crescent
 * @date : 2020-09-23 11:51
 * @description : 在一个时间段内才能执行的节点，在设给定的时间段内时，子节点失败则该节点失败，子节点成功则该节点成功，子节点运行则该节点运行
 * 当不在设定的时间段时，子节点无论是什么状态，该装饰节点都会处于成功状态
 */
public class PeriodSuccess<E> extends LoopDecoratorNode<E> {

    @NodeAttribute(name = "periodStartTime")
    private final LocalDateTime periodStartTime;

    @NodeAttribute(name = "periodEndTime")
    private final LocalDateTime periodEndTime;

    public PeriodSuccess(LocalDateTime periodStartTime, LocalDateTime periodEndTime) {
        this.periodStartTime = periodStartTime;
        this.periodEndTime = periodEndTime;
    }

    public PeriodSuccess(INode<E> child, LocalDateTime periodStartTime, LocalDateTime periodEndTime) {
        super(child);
        this.periodStartTime = periodStartTime;
        this.periodEndTime = periodEndTime;
    }

    @Override
    public boolean condition() {
        final LocalDateTime now = LocalDateTime.now();
        return loop && now.compareTo(periodStartTime) >= 0 && now.compareTo(periodEndTime) <= 0;
    }

    @Override
    public void childFail(NodeContext<E> context) {
        if(condition()){
            fail();
        }else{
            success();
        }
        loop = false;
    }

    @Override
    public void childRunning(NodeContext<E> context, INode<E> reporter) {
        if(condition()){
            running();
        }else{
            success();
        }
        loop = false;
    }

    @Override
    public void childSuccess(NodeContext<E> context) {
        success();
        loop = false;
    }
}
