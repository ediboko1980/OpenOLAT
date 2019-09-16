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
package org.olat.course.learningpath.ui;

import static org.olat.core.gui.components.util.KeyValues.entry;

import java.util.Arrays;

import org.olat.core.gui.UserRequest;
import org.olat.core.gui.components.form.flexible.FormItemContainer;
import org.olat.core.gui.components.form.flexible.elements.SingleSelection;
import org.olat.core.gui.components.form.flexible.elements.TextElement;
import org.olat.core.gui.components.form.flexible.impl.FormBasicController;
import org.olat.core.gui.components.form.flexible.impl.FormEvent;
import org.olat.core.gui.components.util.KeyValues;
import org.olat.core.gui.control.Controller;
import org.olat.core.gui.control.Event;
import org.olat.core.gui.control.WindowControl;
import org.olat.core.util.StringHelper;
import org.olat.modules.ModuleConfiguration;
import org.olat.modules.assessment.model.AssessmentObligation;

/**
 * 
 * Initial date: 27 Aug 2019<br>
 * @author uhensler, urs.hensler@frentix.com, http://www.frentix.com
 *
 */
public class LearningPathNodeConfigController extends FormBasicController {	

	public static final String CONFIG_KEY_ESTIMATED_DURATION = "learning.path.estimated.duration";
	public static final String CONFIG_KEY_OBLIGATION = "learning.path.obligation";
	public static final String CONFIG_DEFAULT_OBLIGATION = AssessmentObligation.mandatory.name();
	public static final String CONFIG_KEY_DONE_TRIGGER = "learning.path.done.trigger";
	public static final String CONFIG_VALUE_DONE_TRIGGER_NONE = "none";
	public static final String CONFIG_VALUE_DONE_TRIGGER_NODE_VISITED = "nodeVisited";
	public static final String CONFIG_VALUE_DONE_TRIGGER_CONFIRMED = "confirmed";
	public static final String CONFIG_VALUE_DONE_TRIGGER_RUN_DONE = "runStatusDone";
	public static final String CONFIG_DEFAULT_DONE_TRIGGER = CONFIG_VALUE_DONE_TRIGGER_NONE;
	
	private TextElement estimatedDurationEl;
	private SingleSelection obligationEl;
	private SingleSelection doneTriggerEl;

	private final ModuleConfiguration configs;
	private final LearningPathControllerConfig ctrlConfig;

	public LearningPathNodeConfigController(UserRequest ureq, WindowControl wControl,
			ModuleConfiguration configs, LearningPathControllerConfig ctrlConfig) {
		super(ureq, wControl);
		this.configs = configs;
		this.ctrlConfig = ctrlConfig;
		initForm(ureq);
	}

	@Override
	protected void initForm(FormItemContainer formLayout, Controller listener, UserRequest ureq) {
		String estimatedTime = configs.getStringValue(CONFIG_KEY_ESTIMATED_DURATION);
		estimatedDurationEl = uifactory.addTextElement("config.estimated.duration", 128, estimatedTime , formLayout);
		
		KeyValues obligationKV = new KeyValues();
		obligationKV.add(entry(AssessmentObligation.mandatory.name(), translate("config.obligation.mandatory")));
		obligationKV.add(entry(AssessmentObligation.optional.name(), translate("config.obligation.optional")));
		obligationEl = uifactory.addRadiosHorizontal("config.obligation", formLayout, obligationKV.keys(), obligationKV.values());
		String obligationKey = configs.getStringValue(CONFIG_KEY_OBLIGATION, CONFIG_DEFAULT_OBLIGATION);
		if (Arrays.asList(obligationEl.getKeys()).contains(obligationKey)) {
			obligationEl.select(obligationKey, true);
		}
		
		KeyValues doneTriggerKV = getDoneTriggerKV();
		doneTriggerEl = uifactory.addDropdownSingleselect("config.done.trigger", formLayout,
				doneTriggerKV.keys(), doneTriggerKV.values());
		doneTriggerEl.addActionListener(FormEvent.ONCHANGE);
		String doneTriggerKey = configs.getStringValue(CONFIG_KEY_DONE_TRIGGER, CONFIG_DEFAULT_DONE_TRIGGER);
		if (Arrays.asList(doneTriggerEl.getKeys()).contains(doneTriggerKey)) {
			doneTriggerEl.select(doneTriggerKey, true);
		}
		
		uifactory.addFormSubmitButton("save", formLayout);
	}
	
	private KeyValues getDoneTriggerKV() {
		KeyValues doneTriggerKV = new KeyValues();
		doneTriggerKV.add(entry(CONFIG_VALUE_DONE_TRIGGER_NONE, translate("config.done.trigger.none")));
		if (ctrlConfig.isDoneTriggerNodeVisited()) {
			doneTriggerKV.add(entry(CONFIG_VALUE_DONE_TRIGGER_NODE_VISITED, translate("config.done.trigger.visited")));
		}
		if (ctrlConfig.isDoneTriggerConfirmed()) {
			doneTriggerKV.add(entry(CONFIG_VALUE_DONE_TRIGGER_CONFIRMED, translate("config.done.trigger.confirmed")));
		}
		TranslateableBoolean doneTriggerRunDone = ctrlConfig.getDoneTriggerRunDone();
		if (doneTriggerRunDone.isTrue()) {
			doneTriggerKV.add(entry(CONFIG_VALUE_DONE_TRIGGER_RUN_DONE,
					getTranslationOrDefault(doneTriggerRunDone, "config.done.trigger.run.done")));
		}
		return doneTriggerKV;
	}
	
	private String getTranslationOrDefault(TranslateableBoolean trans, String defaulI18nKey) {
		return trans.isTranslated()
				? trans.getMessage()
				: translate(defaulI18nKey);
	}
	
	@Override
	protected boolean validateFormLogic(UserRequest ureq) {
		boolean allOk = true;
		
		allOk = validateInteger(estimatedDurationEl, 1, 10000, "error.positiv.int");
		
		return allOk & super.validateFormLogic(ureq);
	}
	
	public static boolean validateInteger(TextElement el, int min, int max, String i18nKey) {
		boolean allOk = true;
		el.clearError();
		if(el.isEnabled() && el.isVisible()) {
			String val = el.getValue();
			if(StringHelper.containsNonWhitespace(val)) {
				
				try {
					int value = Integer.parseInt(val);
					if(min > value) {
						el.setErrorKey(i18nKey, null);
						allOk = false;
					} else if(max < value) {
						el.setErrorKey(i18nKey, null);
						allOk = false;
					}
				} catch (NumberFormatException e) {
					el.setErrorKey(i18nKey, null);
					allOk = false;
				}
			}
		}
		return allOk;
	}

	@Override
	protected void formOK(UserRequest ureq) {
		String estimatedTime = estimatedDurationEl.getValue();
		configs.setStringValue(CONFIG_KEY_ESTIMATED_DURATION, estimatedTime);
		
		String obligation = obligationEl.isOneSelected()
				? obligationEl.getSelectedKey()
				: CONFIG_DEFAULT_OBLIGATION;
		configs.setStringValue(CONFIG_KEY_OBLIGATION, obligation);
		
		String doneTrigger = doneTriggerEl.isOneSelected()
				? doneTriggerEl.getSelectedKey()
				: CONFIG_DEFAULT_DONE_TRIGGER;
		configs.setStringValue(CONFIG_KEY_DONE_TRIGGER, doneTrigger);
		
		fireEvent(ureq, Event.DONE_EVENT);
	}

	@Override
	protected void doDispose() {
		//
	}
	
	public interface LearningPathControllerConfig {
		
		public boolean isDoneTriggerNodeVisited();
		
		public boolean isDoneTriggerConfirmed();
		
		public TranslateableBoolean getDoneTriggerRunDone();
		
	}
	
	public static ControllerConfigBuilder builder() {
		return new ControllerConfigBuilder();
	}
	
	public static class ControllerConfigBuilder {
		
		private boolean doneTriggerNodeVisited;
		private boolean doneTriggerConfirmed;
		private TranslateableBoolean doneTriggerRunDone;
		
		private ControllerConfigBuilder() {
		}
		
		public ControllerConfigBuilder enableNodeVisited() {
			doneTriggerNodeVisited = true;
			return this;
		}
		
		public ControllerConfigBuilder enableRunStatusDone() {
			doneTriggerRunDone = TranslateableBoolean.untranslatedTrue();
			return this;
		}
		
		public ControllerConfigBuilder enableRunStatusDone(String message) {
			doneTriggerRunDone = TranslateableBoolean.translatedTrue(message);
			return this;
		}
		
		public LearningPathControllerConfig build() {
			return new ControllerConfigImpl(this);
		}
		
		private final static class ControllerConfigImpl implements LearningPathControllerConfig {
			
			public final boolean doneTriggerNodeVisited;
			public final boolean doneTriggerConfirmed;
			public final TranslateableBoolean doneTriggerRunDone;

			public ControllerConfigImpl(ControllerConfigBuilder builder) {
				this.doneTriggerNodeVisited = builder.doneTriggerNodeVisited;
				this.doneTriggerConfirmed = builder.doneTriggerConfirmed;
				this.doneTriggerRunDone = falseIfNull(builder.doneTriggerRunDone);
			}
			
			private TranslateableBoolean falseIfNull(TranslateableBoolean translateableBoolean) {
				return translateableBoolean != null
						? translateableBoolean
						: TranslateableBoolean.untranslatedFalse();
			}

			@Override
			public boolean isDoneTriggerNodeVisited() {
				return doneTriggerNodeVisited;
			}

			@Override
			public boolean isDoneTriggerConfirmed() {
				return doneTriggerConfirmed;
			}

			@Override
			public TranslateableBoolean getDoneTriggerRunDone() {
				return doneTriggerRunDone;
			}
			
		}
		
	}
	
}

