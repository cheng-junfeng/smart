/**
 * Copyright 2014 Jeroen Mols
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.media.videocapture.camera;

import com.media.utils.LogUtil;

public class PrepareCameraException extends Exception {

	private static final String	LOG_PREFIX			= "Unable to unlock camera - ";
	private static final String	MESSAGE				= "Unable to use camera for recording";

	private static final long	serialVersionUID	= 6305923762266448674L;

	@Override
	public String getMessage() {
		LogUtil.e(LogUtil.EXCEPTION, LOG_PREFIX + MESSAGE);
		return MESSAGE;
	}
}
