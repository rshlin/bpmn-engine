/**
 * *******************************************************
 * Copyright (C) 2013 catify <info@catify.com>
 * *******************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * 
 */
package com.catify.processengine.core.data.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.catify.processengine.core.data.model.entities.FlowNode;
import com.catify.processengine.core.data.model.entities.FlowNodeInstance;
import com.catify.processengine.core.data.repositories.FlowNodeInstanceRepository;
import com.catify.processengine.core.data.repositories.FlowNodeRepository;

/**
 * As this is a DAO only the critical methods will be tested (mostly spring data involving methods). 
 * 
 * @author chris
 * 
 */
public class FlowNodeInstanceTest extends ModelTestBase {

	@Autowired
	private FlowNodeInstanceRepository flowNodeInstanceRepository;
	
	@Autowired
	private FlowNodeRepository flowNodeRepository;

	@Test
	public void testSaveAndLoad() {

		// create a flow node instance
		FlowNodeInstance flowNodeInstance = createFlowNodeInstance(NodeInstaceStates.ACTIVE_STATE);

		// save it to the db
		flowNodeInstanceRepository.save(flowNodeInstance);

		// try to load it from the db
		flowNodeInstance = flowNodeInstanceRepository.findOne(flowNodeInstance
				.getGraphId());

		// check if the flow node instance could be loaded
		assertNotNull(flowNodeInstance);
	}

	@Test
	public void testInstanceOf() {

		// create a flow node instance
		FlowNodeInstance flowNodeInstance = createFlowNodeInstance(NodeInstaceStates.ACTIVE_STATE);
		flowNodeInstanceRepository.save(flowNodeInstance);

		// create a FlowNode object
		FlowNode flowNode = createFlowNode(UNIQUE_FLOWNODE_ID, FLOWNODE_ID,
				FlOWNODE_TYPE, FLOWNODE_NAME);
		assertNotNull(flowNode);
		flowNodeRepository.save(flowNode);

		// create instanceOf relationship 
		flowNodeInstance.addAsInstanceOf(flowNode, "TESTINSTANCEID");
		flowNodeInstanceRepository.save(flowNodeInstance);
		
		// the flow node should now be accessible through the instanceOf relationship
		assertEquals(flowNode, flowNodeInstance.getHasInstanceRelationship().getNode());
	}
	
	@Test
	public void testFollowingInstance() {

		// create a flow node instance
		FlowNodeInstance flowNodeInstance = createFlowNodeInstance(NodeInstaceStates.ACTIVE_STATE);
		flowNodeInstanceRepository.save(flowNodeInstance);

		// create a second flow node instance
		FlowNodeInstance followingInstance = createFlowNodeInstance(NodeInstaceStates.ACTIVE_STATE);
		flowNodeInstanceRepository.save(followingInstance);


		// create followingInstance relationship 
		flowNodeInstance.addFollowingInstance(followingInstance);
		flowNodeInstanceRepository.save(flowNodeInstance);
		
		// the following flow node instance should now be accessible through the followingInstance relationship
		assertTrue(flowNodeInstance.getFollowingInstances().contains(followingInstance));
	}
	
}
