/**
    Copyright (C) 2010  Holger Dammertz

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package engine.graphics.synthesis.texture;

import engine.base.Vector3;
import engine.base.Vector4;
import engine.parameters.FloatParam;

public final class FilterNormalMap extends Channel {
	FloatParam strength = CreateLocalFloatParam("Strength", 1.0f, 0.0f, Float.MAX_VALUE).setDefaultIncrement(0.125f);
	
	public String getName() {
		return "Normal Map";
	}


	public FilterNormalMap() {
		super(1);
	}
	
	public OutputType getOutputType() {
		return OutputType.RGBA;
	}
	
	public OutputType getChannelInputType(int idx) {
		if (idx == 0) return OutputType.SCALAR;
		else System.err.println("Invalid channel access in " + this);
		return OutputType.SCALAR;
	}
	
	private final Vector4 _function(float du, float dv) {
		Vector3 n = new Vector3(du*strength.get(), dv*strength.get(), 1.0f);
		n.normalize();
		
		Vector4 c = new Vector4(n.x * 0.5f + 0.5f, n.y * 0.5f + 0.5f, n.z * 0.5f + 0.5f, 1.0f);
		return c;
	}
	
	protected void cache_function(Vector4 out, CacheEntry[] ce, float u, float v) {
		out.set(_function(ce[0].du(u, v).XYZto1f(), ce[0].dv(u, v).XYZto1f()));
	}
	
	
	protected float _value1f(float u, float v) {
		Vector4 val = valueRGBA(u, v);
		return (val.x+val.y+val.z)*(1.0f/3.0f);
	}
	
	protected Vector4 _valueRGBA(float u, float v) {
		return _function(inputChannels[0].du1f(u, v).XYZto1f(), inputChannels[0].dv1f(u, v).XYZto1f());
	}
}
