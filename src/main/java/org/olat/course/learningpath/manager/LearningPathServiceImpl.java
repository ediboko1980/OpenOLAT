/**
 * <a href="http://www.openolat.org">
 * OpenOLAT - Online Learning and Training</a><br>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); <br>
 * you may not use this file except in compliance with the License.<br>
 * You may obtain a copy of the License at the
 * <a href="http://www.apache.org/licenses/LICENSE-2.0">Apache homepage</a>
 * <p>
 * Unless required by applicable law or agreed to in writing,<br>
 * software distributed under the License is distributed on an "AS IS" BASIS, <br>
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. <br>
 * See the License for the specific language governing permissions and <br>
 * limitations under the License.
 * <p>
 * Initial code contributed and copyrighted by<br>
 * frentix GmbH, http://www.frentix.com
 * <p>
 */
package org.olat.course.learningpath.manager;

import java.util.List;

import org.olat.core.id.Identity;
import org.olat.core.util.tree.TreeVisitor;
import org.olat.course.CourseFactory;
import org.olat.course.ICourse;
import org.olat.course.learningpath.LearningPathConfigs;
import org.olat.course.learningpath.LearningPathService;
import org.olat.course.nodes.CollectingVisitor;
import org.olat.course.nodes.CourseNode;
import org.olat.course.tree.CourseEditorTreeModel;
import org.olat.repository.RepositoryEntry;
import org.olat.repository.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * Initial date: 1 Sep 2019<br>
 * @author uhensler, urs.hensler@frentix.com, http://www.frentix.com
 *
 */
@Service
public class LearningPathServiceImpl implements LearningPathService {
	
	@Autowired
	private LearningPathRegistry registry;
	@Autowired
	private RepositoryService respositoryService;

	@Override
	public LearningPathConfigs getConfigs(CourseNode courseNode) {
		return registry.getLearningPathNodeHandler(courseNode).getConfigs(courseNode);
	}

	@Override
	public List<CourseNode> getUnsupportedCourseNodes(ICourse course) {
		CourseEditorTreeModel editorTreeModel = course.getEditorTreeModel();
		CollectingVisitor visitor = CollectingVisitor.applying(new UnsupportedFunction(editorTreeModel));
		TreeVisitor tv = new TreeVisitor(visitor, editorTreeModel.getRootNode(), true);
		tv.visitAll();
		return visitor.getCourseNodes();
	}

	@Override
	public RepositoryEntry migrate(RepositoryEntry courseEntry, Identity identity) {
		String displayname = courseEntry.getDisplayname() + " (copy)";
		RepositoryEntry lpEntry = respositoryService.copy(courseEntry, identity, displayname);
		CourseFactory.loadCourse(lpEntry).getCourseConfig().setNodeAccessType(LearningPathNodeAccessProvider.TYPE);
		return lpEntry;
	}
	
}
