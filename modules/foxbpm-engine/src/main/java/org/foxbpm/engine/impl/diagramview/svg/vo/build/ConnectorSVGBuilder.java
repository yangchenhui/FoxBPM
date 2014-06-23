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
 * @author MAENLIANG
 */
package org.foxbpm.engine.impl.diagramview.svg.vo.build;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.foxbpm.engine.exception.FoxBPMException;
import org.foxbpm.engine.impl.diagramview.svg.Point;
import org.foxbpm.engine.impl.diagramview.svg.PointUtils;
import org.foxbpm.engine.impl.diagramview.svg.SVGUtils;
import org.foxbpm.engine.impl.diagramview.svg.vo.PathVO;
import org.foxbpm.engine.impl.diagramview.svg.vo.SvgVO;

/**
 * 默认线条式样构造
 * 
 * @author MAENLIANG
 * @date 2014-06-10
 */
public class ConnectorSVGBuilder extends AbstractSVGBuilder {
	private static final String FILL_DEFAULT = "none";
	private static final String MOVETO_FLAG = "M";
	private static final String LINETO_FLAG = "L";
	private static final String PATHCIRCLE_FLAG = "C";

	private static final String D_SPACE = " ";
	private PathVO pathVo;

	public ConnectorSVGBuilder(SvgVO svgVo) {
		super(svgVo);
		this.textVO = svgVo.getgVo().getgVoList().get(0).getTextVo();
		this.pathVo = SVGUtils.getSequenceVOFromSvgVO(svgVo);
		if (pathVo == null) {
			throw new FoxBPMException("线条元素初始化工厂时候报错，pathVo对象为空");
		}

	}

	/**
	 * 设置线条的拐点信息,拐角划圆形
	 */
	public void setWayPoints(List<Point> pointList) {
		StringBuffer pathBuffer = new StringBuffer();
		int size = pointList.size();
		for (int i = 0; i < size; i++) {
			Point point = pointList.get(i);
			// 第二个坐标
			if (i != 0) {
				// 判断是否只有两个坐标
				if (size < 3) {
					pathBuffer.append(LINETO_FLAG)
							.append(String.valueOf(point.getX()))
							.append(D_SPACE)
							.append(String.valueOf(point.getY()));
				} else {
					if (i != size - 1) {
						// 以当前坐标为中心点
						Point[] bezalPoints = PointUtils.caclBeralPoints(
								pointList.get(i - 1), pointList.get(i),
								pointList.get(i + 1));
						if (bezalPoints[0].getX() != 0.0f
								&& bezalPoints[0].getY() != 0.0f) {
							pathBuffer.append(LINETO_FLAG).append(
									bezalPoints[0].getX() + D_SPACE
											+ bezalPoints[0].getY() + D_SPACE);
						}

						if (bezalPoints[1].getX() != 0.0f
								&& bezalPoints[1].getY() != 0.0f
								&& bezalPoints[2].getX() != 0.0f
								&& bezalPoints[2].getY() != 0.0f
								&& bezalPoints[3].getX() != 0.0f
								&& bezalPoints[3].getY() != 0.0f) {
							pathBuffer
									.append(PATHCIRCLE_FLAG)
									.append(bezalPoints[1].getX() + D_SPACE
											+ bezalPoints[1].getY() + D_SPACE)
									.append(bezalPoints[2].getX() + D_SPACE
											+ bezalPoints[2].getY() + D_SPACE)
									.append(bezalPoints[3].getX() + D_SPACE
											+ bezalPoints[3].getY() + D_SPACE);
						}

						pathBuffer.append(LINETO_FLAG)
								.append(pointList.get(i + 1).getX())
								.append(D_SPACE)
								.append(pointList.get(i + 1).getY());
					}
				}

			} else {
				pathBuffer.append(MOVETO_FLAG)
						.append(String.valueOf(point.getX())).append(D_SPACE)
						.append(String.valueOf(point.getY()));
			}

		}
		this.pathVo.setD(pathBuffer.toString());
	}

	@Override
	public void setStroke(String stroke) {
		if (StringUtils.isBlank(stroke)) {
			this.pathVo.setStroke(STROKE_DEFAULT);
			return;
		}
		this.pathVo.setStroke(COLOR_FLAG + stroke);
	}

	@Override
	public void setStrokeWidth(float strokeWidth) {
		this.pathVo.setStrokeWidth(strokeWidth);

	}

	@Override
	public void setFill(String fill) {
		if (StringUtils.isBlank(fill)) {
			this.pathVo.setFill(FILL_DEFAULT);
			return;
		}
		this.pathVo.setFill(COLOR_FLAG + fill);

	}

	@Override
	public void setID(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setStyle(String style) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTypeStroke(String stroke) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTypeStrokeWidth(float strokeWidth) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTypeFill(String fill) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTypeStyle(String style) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setWidth(float width) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setHeight(float height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setXAndY(float x, float y) {
	}
}
