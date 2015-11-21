package com.gdx.orion.shaders;

	import com.badlogic.gdx.Gdx;
	import com.badlogic.gdx.graphics.Camera;
	import com.badlogic.gdx.graphics.GL20;
	import com.badlogic.gdx.graphics.Texture;
	import com.badlogic.gdx.graphics.Texture.TextureFilter;
	import com.badlogic.gdx.graphics.Texture.TextureWrap;
	import com.badlogic.gdx.graphics.g3d.Renderable;
	import com.badlogic.gdx.graphics.g3d.Shader;
	import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
	import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
	import com.badlogic.gdx.graphics.glutils.ShaderProgram;
	import com.badlogic.gdx.math.Matrix3;
	import com.badlogic.gdx.math.Matrix4;
	import com.badlogic.gdx.utils.GdxRuntimeException;

	public class AsteroidShader implements Shader {
		ShaderProgram program;
		Camera camera;
		RenderContext context;
		
		int u_projTrans;
		int u_worldTrans;
		int u_color;
		Texture grass = new Texture("terrain/grass.png");
		Texture rock  = new Texture("terrain/rock.png");
		Texture dirt  = new Texture("terrain/dirt.png");
		Texture splat = new Texture("terrain/terrain.png");
		private final Matrix3 normalMatrix = new Matrix3();
		private static final float[] lightPosition = { 205, 135, 5 };
		private Matrix4 modelView = new Matrix4();
		
		@Override
		public void init() {
	        String vert = Gdx.files.internal("shaders/vertex/terrain_vertex.vsh").readString();
	        String frag = Gdx.files.internal("shaders/fragment/terrain_fragment.fsh").readString();
	        program = new ShaderProgram(vert, frag);
	        if (!program.isCompiled()){
	        	throw new GdxRuntimeException(program.getLog());
	        }else{
	        	System.out.println("COMPILED SHADER PROGRAM");
	        }

	        u_projTrans = program.getUniformLocation("u_projTrans");
	        u_worldTrans = program.getUniformLocation("u_worldTrans");
	        u_color = program.getUniformLocation("u_color");

		}

		@Override
		public void dispose() {
			program.dispose();
			grass.dispose();
			rock.dispose();
			dirt.dispose();
			splat.dispose();
		}

		@Override
		public void begin(Camera camera, RenderContext context) {
			this.camera = camera;
			this.context = context;
			program.begin();
			program.setUniformMatrix(u_projTrans, camera.combined);
			context.setDepthTest(GL20.GL_LEQUAL);
			context.setCullFace(GL20.GL_BACK);
		}

		@Override
		public void render(Renderable renderable){
			lightPosition[0] = 0;
			lightPosition[1] = 0;
			lightPosition[2] = 0;
			//bind correct textures		
		    modelView.set(renderable.worldTransform);
			program.setUniformf("offsetU", ((TextureAttribute)(renderable.material.get(TextureAttribute.Diffuse))).offsetU);
			program.setUniformf("offsetV", ((TextureAttribute)(renderable.material.get(TextureAttribute.Diffuse))).offsetV);
			program.setUniformf("scaleU", ((TextureAttribute)(renderable.material.get(TextureAttribute.Diffuse))).scaleU);
			program.setUniformf("scaleV", ((TextureAttribute)(renderable.material.get(TextureAttribute.Diffuse))).scaleV);
			
			program.setUniformMatrix("u_normalMatrix", normalMatrix.set(modelView).inv().transpose());
			program.setUniform3fv("u_lightPosition", lightPosition , 0, 3);
			//program.setUniform4fv("u_ambientColor", ambientColor, 0, 4);
			//program.setUniform4fv("u_diffuseColor", diffuseColor, 0, 4);
			//program.setUniform4fv("u_specularColor", specularColor, 0, 4);
			
			program.setUniformi("u_texture3", context.textureBinder.bind(dirt));
			dirt.unsafeSetWrap(TextureWrap.Repeat,TextureWrap.Repeat);
			dirt.unsafeSetFilter(TextureFilter.Nearest, TextureFilter.Nearest);
			program.setUniformi("u_texture2", context.textureBinder.bind(rock));
			rock.unsafeSetWrap(TextureWrap.Repeat,TextureWrap.Repeat);
			rock.unsafeSetFilter(TextureFilter.Nearest, TextureFilter.Nearest);
			program.setUniformi("u_texture1", context.textureBinder.bind(grass));
			grass.unsafeSetWrap(TextureWrap.Repeat,TextureWrap.Repeat);
			grass.unsafeSetFilter(TextureFilter.Nearest, TextureFilter.Nearest);
			program.setUniformi("u_texture0", context.textureBinder.bind(((TextureAttribute)(renderable.material.get(TextureAttribute.Diffuse))).textureDescription));
			
			
			program.setUniformMatrix(u_worldTrans, renderable.worldTransform);
		}

		@Override
		public void end() {
			program.end();
		}

		@Override
		public int compareTo(Shader other) {
			return 0;
		}
		@Override
		public boolean canRender(Renderable instance) {
			return true;
		}
		public ShaderProgram getProgram(){
			return program;
		}
	}