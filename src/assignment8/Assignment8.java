package assignment8;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;

import javax.media.j3d.Alpha;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.IndexedQuadArray;
import javax.media.j3d.Material;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import com.sun.j3d.utils.universe.SimpleUniverse;


public class Assignment8 extends Applet {

	  public static void main(String[] args) {
	        System.setProperty("sun.awt.noerasebackground", "true");
	        new MainFrame(new Assignment8(), 640, 480);
	    }

	    public void init() {
	        // create canvas
	        GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();
	        Canvas3D cv = new Canvas3D(gc);
	        setLayout(new BorderLayout());
	        add(cv, BorderLayout.CENTER);
	        BranchGroup bg = createBranchGraph();
	        bg.compile();
	        SimpleUniverse su = new SimpleUniverse(cv);
	        su.getViewingPlatform().setNominalViewingTransform();
	        su.addBranchGraph(bg);
	    }

	    private BranchGroup createBranchGraph() {
	        BranchGroup root = new BranchGroup();
	        
	     //Create the blue background
			Background background = new Background(new Color3f(1f,1f,1f));
			BoundingSphere sphere = new BoundingSphere(new Point3d(0,0,0), 100000); //background is inside this sphere
			background.setApplicationBounds(sphere);
			root.addChild(background); //add to the scene
			
		//Create a color that shines on object
			Color3f lightColor=new Color3f(.75f,.75f,.75f);
			//Light is 100m away from origin
			BoundingSphere boundSphere=new BoundingSphere(new Point3d(0,0,0),100);
			//Create a vector for lights direction
			Vector3f direction=new Vector3f(4.0f,-7.0f,-12.0f);
			//Actually create the light
			DirectionalLight light=new DirectionalLight(lightColor,direction);
			light.setInfluencingBounds(boundSphere);
			//add to group
			root.addChild(light);
			
	        TransformGroup spin = new TransformGroup();
	        spin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	        root.addChild(spin);
	        //object
	        Appearance ap = new Appearance();
	        ap.setPolygonAttributes(new PolygonAttributes(
	                PolygonAttributes.POLYGON_LINE, PolygonAttributes.CULL_NONE, 0));
	        Shape3D shape = new Shape3D();
	        shape.setGeometry(mobius().getIndexedGeometryArray());
	        Appearance a=new Appearance();
	        Material m=new Material();
	        m.setAmbientColor(0.25f, 0.25f, 0.25f);
	        a.setMaterial(m);
	        shape.setAppearance(a);
	        //transform
	        Transform3D tr = new Transform3D();
	        tr.setScale(0.25);
	        tr.setTranslation(new Vector3d(0, -0.25, 0));
	        TransformGroup tg = new TransformGroup(tr);
	        spin.addChild(tg);
	        tg.addChild(shape);
	        //animation
	        Alpha alpha = new Alpha(-1, 4000);
	        RotationInterpolator rotator = new RotationInterpolator(alpha, spin);
	        BoundingSphere bounds = new BoundingSphere();
	        rotator.setSchedulingBounds(bounds);
	        spin.addChild(rotator);
	        return root;
	    }
	
	private GeometryInfo mobius()
	{
		
		int row=100;
		int col=100;
		
		int p=4*((row-1)*(col-1));
		
		IndexedQuadArray arr=new IndexedQuadArray(row*col,GeometryArray.COORDINATES,p);
		
		Point3d[] vertices=new Point3d[row*col];
		int index=0;
		
		//Create the vertices
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<col;j++)
			{
				double u = i * (4*(Math.PI))/(row - 1);
                double v = -0.3 + (j * (0.6/(col-1)));
                double x=(1+v*Math.cos(u/2))*Math.cos(u);
                double y=(1+v*Math.cos(u/2))*Math.sin(u);
                double z=v*Math.sin(u/2);
                vertices[index]=new Point3d(x,y,z);
                index++;
			}
		}
		
		arr.setCoordinates(0, vertices);
		index=0;
		
		
		//set index for coordinates
        for(int i = 0; i < row-1; i++){
            for(int j = 0; j < col-1; j++){
                arr.setCoordinateIndex(index, i*row+j);
                index++;
                arr.setCoordinateIndex(index, i*row+j+1);
                index++;
                arr.setCoordinateIndex(index, (i+1)*row+j+1);
                index++;
                arr.setCoordinateIndex(index, (i+1)*row+j);
                index++;
            }
        }
        
        GeometryInfo g=new GeometryInfo(arr);
        NormalGenerator norm = new NormalGenerator();
        norm.generateNormals(g);
		return g;
		
	}

}
