//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 15MAR2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.projection;

//--------------------------------- IMPORTS ------------------------------------

////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 15MAR2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * ProjectionException class.
 * <hr>
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 15/03/08
 * @author      Guidebee Pty Ltd.
 */
public class ProjectionException extends RuntimeException {
	public ProjectionException() {
		super();
	}

	public ProjectionException(String message) {
		super(message);
	}
}
