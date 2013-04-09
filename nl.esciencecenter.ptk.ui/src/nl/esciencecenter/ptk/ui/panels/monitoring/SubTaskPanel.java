/*
 * (C) 2012 Netherlands eScience Center/Biomarker Boosting consortium. 
 * 
 * This code is under development. 
 *  
 */ 
// source: 

package nl.esciencecenter.ptk.ui.panels.monitoring;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import nl.esciencecenter.ptk.presentation.Presentation;
import nl.esciencecenter.ptk.task.ActionTask;
import nl.esciencecenter.ptk.task.ITaskMonitor;
import nl.esciencecenter.ptk.task.MonitorStats;
import nl.esciencecenter.ptk.task.TaskWatcher;
import nl.esciencecenter.ptk.task.ITaskMonitor.TaskStats;
import nl.esciencecenter.ptk.util.logging.ClassLogger;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
/**
 * Mini Status Panel
 */

public class SubTaskPanel extends JPanel implements ActionListener 
{
    private static final long serialVersionUID = -6694878602014623166L;
    
    private JTextField titleTextField;
    private JLabel destLabel;
    private ProgresPanel statusPanel;
   // private JTextField destTF;
  //  private JTextField sourceTF;
    private JLabel sourceLabel;
    private JPanel transferInfo;

    private Presentation presentation=Presentation.createDefault(); 
    private ITaskMonitor taskMonitor=null;
    private MonitorStats monitorStats; 
    private boolean showTransfersSpeeds=true;
    
    // constructor for Jigloo 
    public SubTaskPanel()
    {
        super(); 
        initGUI(); 
    }
    
    public SubTaskPanel(ITaskMonitor monitor)
    {
        super();
        initGUI();
        setMonitor(monitor);
    }
    
    public void setMonitor(ITaskMonitor monitor)
    {
        this.taskMonitor=monitor;  
        this.monitorStats=new MonitorStats(taskMonitor);
        // update at start to initialize fields: 
        update(); 
    }

    /** Whether speeds in [GMK]B/s should be shown */ 
    public void setShowTransferSpeed(boolean val)
    {
        this.showTransfersSpeeds=val; 
    }
    
    ActionTask updateTask = new ActionTask(TaskWatcher.getTaskWatcher(),"Monitor Updater Task") 
    {
        
        public void doTask() 
        {
            while (taskMonitor.isDone()==false)
            {
               update();
            
               if (taskMonitor.isDone())
               {
//                  
//                   if (taskMonitor.hasError())
//                   {
//                       browserController.handle(taskMonitor.getException());
//                   }
                //
//                   okButton.setEnabled(true);
//                   cancelButton.setEnabled(false); 
               }
               try
               {
                   Thread.sleep(100);// 10fps 
               }
               catch (InterruptedException e)
               {
                   ClassLogger.getLogger(this.getClass()).errorPrintf("Interrupted!\n"); 
                } 
            }
            
            // task done: final update
            update();
        }

        @Override
        public void stopTask()
        {
            // vfstransfer must stop the transfer. 
            //task.setMustStop();
        }
    };

   
    public void start()
    {
        startUpdater(); 
    }
    
    protected void startUpdater()
    {
        updateTask.startTask(); 
    }
    
    public void stop()
    {
        if (this.updateTask.isAlive())
        {
            this.updateTask.signalTerminate();
        }
    }
    
    protected void update()
    {
        if (taskMonitor==null)
            return; 
        
        String task=this.taskMonitor.getTaskName(); 
        String subTask=this.taskMonitor.getCurrentSubTaskName();
        
        this.titleTextField.setText(task);
       //  this.currentTaskTF.setText(subTask); 
        
       // sourceTF.setText(taskMonitor.getSource().toString()); 
       // destTF.setText(taskMonitor.getDestination().toString()); 
       //  this.statusPanel.setStatusText(monitorStats.getStatus()); 
        this.statusPanel.setProgressText(getTotalProgressText()); 
        this.statusPanel.setProgress(getTotalProgress()); 
        
        if (this.taskMonitor.isDone())
        {
//          this.cancelButton.setEnabled(false); 
//          this.okButton.setEnabled(true);
        }
    }
    
    private double getTotalProgress()
    {
        return ((double)taskMonitor.getTotalWorkDone())/(double)taskMonitor.getTotalWorkTodo(); 
    }


    public String getTitle()
    {
        return this.titleTextField.getText();
    }

    private String getTimers()
    {
        String timestr=presentation.createRelativeTimeString(monitorStats.getTotalDoneTime(),false);
        
        long eta=monitorStats.getETA();
        
        if (eta<0)
        	timestr+=" (?)";
        else if (eta==0) 
        	timestr+= " (done)";
        else
        	timestr+=" ("+presentation.createRelativeTimeString(eta,false)+")";
        
        return timestr; 
        
    }

    protected Container getContentPane()
    {
        return this; 
    }

    private void initGUI() 
    {
        
        try 
        {
            BorderLayout thisLayout = new BorderLayout();
            thisLayout.setHgap(8);
            thisLayout.setVgap(8);
            getContentPane().setLayout(thisLayout);
            this.setPreferredSize(new java.awt.Dimension(320, 94));
            this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            {
                transferInfo = new JPanel();
                getContentPane().add(transferInfo, BorderLayout.CENTER);
                FormLayout transferInfoLayout = new FormLayout(
                        "5dlu, 5dlu, max(d;128dlu):grow, max(d;15dlu), max(d;5dlu)", 
                        "max(p;5dlu), max(p;8dlu), max(p;37dlu), max(p;5dlu)");
                transferInfo.setLayout(transferInfoLayout);
                transferInfo.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
                //transferInfo.setPreferredSize(new java.awt.Dimension(512, 126));
                //transferInfo.setPreferredSize(new java.awt.Dimension(330, 80));
                {
                    titleTextField = new JTextField();
                    transferInfo.add(getStatusPanel(), new CellConstraints("2, 3, 3, 1, default, default"));
                    transferInfo.add(titleTextField, new CellConstraints("2, 2, 2, 1, default, default"));
                    titleTextField.setText("(Sub) Transfer Action Task");
                    titleTextField.setBackground(new java.awt.Color(
                        229,
                        229,
                        229));
                    titleTextField.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // annoying
       // this.setAlwaysOnTop(true); 
        
    }
    
    // === Static === 
    
    /**
     * Auto-generated main method to display this JDialog
     */
     public static void main(String[] args)
     {
         ActionTask task=new ActionTask(null,"StatusPanel background Task")
         {
             public void doTask()
             {
                 int max=1000; 
                 
                 ITaskMonitor monitor = this.getTaskMonitor(); 
                 
                 monitor.startTask("Main Tester Task",max); 
                                  
                 for (int i=0;i<=max;i++)
                 {
                     String subTask="Subtask:"+i; 
                     
                     if ((i%10)==0)
                     {
                         monitor.startSubTask(subTask,10);
                     }
                     
                     monitor.updateSubTaskDone(subTask, i%10); 
                     
                     // panel.setProgress(i); 
                     monitor.updateTaskDone(i); // panel.setProgress((double)i/1000.0); 
                     
                     try
                     {
                         Thread.sleep(60-(i*50)/max);
                     }
                     catch (InterruptedException e)
                     {
                         e.printStackTrace();
                     }
                 }

             }

             @Override
             public void stopTask()
             {
             }
         };
         
         task.startTask();
         
         JFrame frame = new JFrame();
         final SubTaskPanel panel = new SubTaskPanel(task.getTaskMonitor());
         
         panel.start();
         
         frame.add(panel); 
         frame.pack();
         frame.setVisible(true); 
     }

    public void actionPerformed(ActionEvent e)
    {
//        if (e.getSource()==this.okButton)
//            dispose();
//        
//        if (e.getSource()==this.cancelButton)
//        {
//        	// stop already initiated
//        	if (task.isCancelled())
//        		cancelButton.setEnabled(false); 
//        	
//            this.task.signalTerminate();
//        }
    }

    /** return progress information */ 
    public String getTotalProgressText()
    {
        ITaskMonitor info=taskMonitor; 
        
        String progstr="";
        
        if (info.isDone())
        {
            // Final Times
            String str="Done:"+sizeString(info.getTotalWorkDone())
            	+" of "+sizeString(info.getTotalWorkTodo()); 
        	if (showTransfersSpeeds)
                str+=" ("+getTotalSpeedString() +")";
            str+=" in "+presentation.createRelativeTimeString(monitorStats.getTotalDoneTime(),false);
        	
        	return str; 
        }
         
        // Print total transfer info:
        if (info.getTotalWorkTodo()<=0)
        {
            progstr+="(?) ";
        }
        else
        {
            progstr+=percentage3(info.getTotalWorkDone(),info.getTotalWorkTodo())+"% ";
        }
        
        // delta time busy 
        long delta=monitorStats.getTotalDoneLastUpdateTime()-info.getStartTime(); 
        if (delta<=0) 
        	delta=1; 
        
        progstr+="("+sizeString(info.getTotalWorkDone())+" of "+ sizeString(info.getTotalWorkTodo())+")";
        
        // TransferSpeed ONLY for VFS Transfers !
        if (showTransfersSpeeds)
            progstr+=" "+getTotalSpeedString();   
        
        progstr+=" "+getTimers(); 

        return progstr;
    }
    
    private String getTotalSpeedString()
    {
        return sizeString((int)monitorStats.getTotalSpeed())+"B/s";    
    }
    
    /** return progress information */ 
    public String getCurrentProgressText()
    {
    	ITaskMonitor info=taskMonitor; 
        
        String progstr="";
        
        if (info.isDone())
        {
            return "Done.";
        }
        String subTask=monitorStats.getCurrentSubTaskName(); 
        
        // Print Current transfer info:
        
        if (monitorStats.getSubTaskTodo(subTask)<=0)
        {
            progstr="(?) ";
        }
        else
        {
            progstr+=percentage3(monitorStats.getSubTaskDone(subTask),monitorStats.getSubTaskTodo(subTask))+"% ";
        }
        
        long delta=getSubTaskDoneLastUpdateTime()-getSubTaskStartTime();
        
        if (delta<=0) 
            delta=1; 
        
        progstr+=presentation.sizeString(monitorStats.getSubTaskDone(subTask))+" "
            +"("+monitorStats.getSubTaskDone(subTask)/delta+"Work/s)";
                 
        return progstr;
    }


   

    public long getSubTaskStartTime()
    {
        TaskStats subStats = this.taskMonitor.getSubTaskStats(taskMonitor.getCurrentSubTaskName()); 
        
        if (subStats==null)
            return 0;
        
        return subStats.startTimeMillies;
    }

    public long getSubTaskDoneLastUpdateTime()
    {
        TaskStats subStats = this.taskMonitor.getSubTaskStats(taskMonitor.getCurrentSubTaskName()); 
        
        if (subStats==null)
            return 0;
        
        return subStats.doneLastUpdateTimeMillies;
    }

    /** Return percentage in 3 chars */
    private String percentage3(double x,double y) 
    {
        double perc=(x*1000)/y; 
        
        // 100%
        if (perc>=1000) 
            return "100"; 
        
        int intVal=(int)Math.floor(perc); 
        
        // 10-99%
        if (perc>=100)
            return " "+(intVal/10);
        
        // 9.9%
        return (intVal/10)+"."+(perc%10);    
        
    }

    private String sizeString(long size)
    {
    	if (size<0) 
    		return "?";
    	
        return presentation.sizeString(size,true,1,1);
    }
    
    public void dispose()
    {
        stop(); 
    }
    
  
    
    private ProgresPanel getStatusPanel() {
        if(statusPanel == null) {
            statusPanel = new ProgresPanel();
            statusPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        }
        return statusPanel;
    }

}
