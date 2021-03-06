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
package org.olat.modules.bigbluebutton;

import java.util.Date;
import java.util.List;

import org.olat.core.id.Identity;
import org.olat.core.id.Roles;
import org.olat.group.BusinessGroup;
import org.olat.modules.bigbluebutton.model.BigBlueButtonErrors;
import org.olat.modules.bigbluebutton.model.BigBlueButtonServerInfos;
import org.olat.repository.RepositoryEntry;
import org.olat.repository.RepositoryEntryRef;

/**
 * 
 * Initial date: 18 mars 2020<br>
 * @author srosse, stephane.rosse@frentix.com, http://www.frentix.com
 *
 */
public interface BigBlueButtonManager {
	
	public BigBlueButtonServer createServer(String url, String recordingUrl, String sharedSecret);
	
	public BigBlueButtonServer updateServer(BigBlueButtonServer server);
	
	public boolean hasServer(String url);
	
	public List<BigBlueButtonServer> getServers();
	
	public List<BigBlueButtonServerInfos> getServersInfos();
	
	public List<BigBlueButtonServerInfos> filterServersInfos(List<BigBlueButtonServerInfos> infos);
	
	
	public void deleteServer(BigBlueButtonServer server, BigBlueButtonErrors errors);
	
	
	/**
	 * Create and persist a meeting in OpenOlat. The method will generate
	 * an unique meeting identifier and passwords for attendees and moderators.
	 * 
	 * @param name The name of the meeting
	 * @param entry The repository entry (optional but this or group)
	 * @param subIdent The sub-identifier (optional)
	 * @param businessGroup The business group (optional but this or entry)
	 * @return A meeting with some default values
	 */
	public BigBlueButtonMeeting createAndPersistMeeting(String name, RepositoryEntry entry, String subIdent, BusinessGroup businessGroup);
	
	/**
	 * Is there a server available.
	 * 
	 * @param template The selected template
	 * @param start Start date
	 * @param leadTime Lead time
	 * @param end End date
	 * @param followupTime Follow-up time
	 * @return true if the meeting can be reserved
	 */
	public boolean isSlotAvailable(BigBlueButtonMeeting meeting, BigBlueButtonMeetingTemplate template, Date start, long leadTime, Date end, long followupTime);

	public BigBlueButtonMeeting getMeeting(BigBlueButtonMeeting meeting);
	
	public BigBlueButtonMeeting updateMeeting(BigBlueButtonMeeting meeting);
	
	public boolean deleteMeeting(BigBlueButtonMeeting meeting, BigBlueButtonErrors errors);
	
	public BigBlueButtonMeetingTemplate createAndPersistTemplate(String name);

	public List<BigBlueButtonMeetingTemplate> getTemplates();
	
	public List<BigBlueButtonMeetingTemplate> getTemplates(List<BigBlueButtonTemplatePermissions> permissions);

	/**
	 * Calculate the permissions of the specified identity for the repository entry or business group.
	 * 
	 * @param entry The repository entry (optional)
	 * @param businessGroup The business group (optional)
	 * @param identity The identity
	 * @param userRoles The roles of the identity
	 * @return
	 */
	public List<BigBlueButtonTemplatePermissions> calculatePermissions(RepositoryEntry entry, BusinessGroup businessGroup, Identity identity, Roles userRoles);
	
	public BigBlueButtonMeetingTemplate updateTemplate(BigBlueButtonMeetingTemplate template);
	
	public void deleteTemplate(BigBlueButtonMeetingTemplate template);
	
	public boolean isTemplateInUse(BigBlueButtonMeetingTemplate template);
	
	public List<BigBlueButtonMeeting> getMeetings(RepositoryEntryRef entry, String subIdent, BusinessGroup businessGroup);
	
	/**
	 * Return the list of upcoming meetings, without any permanent one.
	 * 
	 * @param entry The course / resource entry
	 * @param subIdent The sub identifier
	 * @return
	 */
	public List<BigBlueButtonMeeting> getUpcomingsMeetings(RepositoryEntryRef entry, String subIdent, int maxResults);
	
	public List<BigBlueButtonMeeting> getAllMeetings();
	
	public String join(BigBlueButtonMeeting meeting, Identity identity, boolean moderator, boolean guest,
			Boolean isRunning, BigBlueButtonErrors errors);
	
	public boolean isMeetingRunning(BigBlueButtonMeeting meeting);
	
	public List<BigBlueButtonRecording> getRecordings(BigBlueButtonMeeting meeting, BigBlueButtonErrors errors);
	
	public boolean checkConnection(String url, String sharedSecret, BigBlueButtonErrors errors);
	
	

}
