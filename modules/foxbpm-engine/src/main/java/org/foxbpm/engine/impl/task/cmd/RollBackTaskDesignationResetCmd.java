/**
 * Copyright 1996-2014 FoxBPM ORG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * @author kenshin
 */
package org.foxbpm.engine.impl.task.cmd;

import org.foxbpm.engine.exception.FoxBPMBizException;
import org.foxbpm.engine.exception.FoxBPMIllegalArgumentException;
import org.foxbpm.engine.impl.entity.TaskEntity;
import org.foxbpm.engine.impl.identity.Authentication;
import org.foxbpm.engine.impl.interceptor.CommandContext;
import org.foxbpm.engine.impl.task.command.RollBackTaskDesignationResetCommand;
import org.foxbpm.engine.impl.util.StringUtil;
import org.foxbpm.engine.task.CommandParam;
import org.foxbpm.engine.task.TaskCommand;
import org.foxbpm.kernel.process.KernelProcessDefinition;
import org.foxbpm.kernel.process.impl.KernelFlowNodeImpl;
import org.foxbpm.kernel.runtime.FlowNodeExecutionContext;

/**
 * @author kenshin
 * 
 */
public class RollBackTaskDesignationResetCmd extends AbstractExpandTaskCmd<RollBackTaskDesignationResetCommand, Void> {

	private static final long serialVersionUID = 1L;

	public RollBackTaskDesignationResetCmd(RollBackTaskDesignationResetCommand abstractCustomExpandTaskCommand) {
		super(abstractCustomExpandTaskCommand);
	}

	@Override
	protected Void execute(CommandContext commandContext, TaskEntity task) {

		/** 获取任务命令 */
		TaskCommand taskCommand = getTaskCommand(task);
		/** 获取流程内容执行器 */
		FlowNodeExecutionContext executionContext = getExecutionContext(task);
		/** 任务命令的执行表达式变量 */
		taskCommand.getExpressionValue(executionContext);

		/** 获取执行任务命令参数 */
		CommandParam commandParam = taskCommand.getCommandParam(RollBackTaskDesignationResetCommand.EXECUTEPARAM_ROLLBACK_NODEID);
		/** 从后台配置中获取需要退回的节点 */
		String rollBackNodeId = StringUtil.getString(commandParam.getExpression().getValue(executionContext));
		
		/** 验证退回节点 */
		if (StringUtil.isEmpty(rollBackNodeId)) {
			throw new FoxBPMBizException("退回的节点号不能为空");
		}
		
		/** 设置任务处理者 */
		task.setAssignee(Authentication.getAuthenticatedUserId());
		/** 设置任务的处理命令 commandId commandName commandType */
		task.setTaskCommand(taskCommand);
		/** 处理意见 */
		task.setTaskComment(taskComment);
		/** 获取流程定义 */
		KernelProcessDefinition processDefinition = getProcessDefinition(task);
		/** 查找需要退回的节点 */
		KernelFlowNodeImpl flowNode = processDefinition.findFlowNode(rollBackNodeId);
		if(flowNode == null){
			throw new FoxBPMIllegalArgumentException("退回的目的节点："+rollBackNodeId+"不存在,请检查流程配置！");
		}
		/** 完成任务,并将流程推向指定的节点 */
		task.complete(flowNode);
		
		return null;
	}

}