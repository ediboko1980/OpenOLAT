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
package org.olat.modules.lecture;

import java.util.Date;

import org.olat.core.id.CreateInfo;
import org.olat.core.id.Identity;
import org.olat.core.id.ModifiedInfo;

/**
 * 
 * 
 * Initial date: 22 juil. 2019<br>
 * @author srosse, stephane.rosse@frentix.com, http://www.frentix.com
 *
 */
public interface AbsenceNotice extends AbsenceNoticeRef, ModifiedInfo, CreateInfo {
	
	public Identity getIdentity();
	
	public AbsenceNoticeType getNoticeType();
	
	public void setNoticeType(AbsenceNoticeType type);
	
	public  AbsenceNoticeTarget getNoticeTarget();
	
	public void setNoticeTarget(AbsenceNoticeTarget target);
	
	public Date getStartDate();
	
	public void setStartDate(Date date);
	
	public Date getEndDate();

	public void setEndDate(Date endDate);
	
	public AbsenceCategory getAbsenceCategory();
	
	public void setAbsenceCategory(AbsenceCategory category);
	
	public String getAbsenceReason();
	
	public void setAbsenceReason(String reason);
	
	public Boolean getAbsenceAuthorized();

	public void setAbsenceAuthorized(Boolean absenceAuthorized);
	
	public Identity getNotifier();
	
	public Identity getAuthorizer();

}
