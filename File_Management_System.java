import java.util.*;

interface FileSystemItem{
    String getName();
    int getSize();
    void ls(int space);
    FileSystemItem cd(String name);
    void openAll(int space);
    boolean isFolder();
}
class File implements FileSystemItem{
    private String name;
    private int size;
    File(String name,int size){
        this.name=name;
        this.size=size;
    }
    public String getName(){
        return name;
    }
    public int getSize(){
        return size;
    }
    public void ls(int space){
        System.out.println(" ".repeat(space)+name);
    }
    public void openAll(int space){
        String indentSpaces = " ".repeat(space);
        System.out.println(indentSpaces + name);
    }
    public FileSystemItem cd(String name){
        return null;
    }
    public boolean isFolder(){
        return false;
    }
}
class Folder implements FileSystemItem{
    private String name;
    
    private List<FileSystemItem> lists=new ArrayList<>();
    Folder(String name){
        this.name=name;
    }
    public void add(FileSystemItem fi){
      lists.add(fi);  
    }
    public String getName(){
        return name;
    }
    public int getSize(){
        int tot=0;
        for(FileSystemItem item:lists){
            tot+=item.getSize();
        }
        return tot;
    }
    public void ls(int space){
        String indentSpaces = " ".repeat(space);
        for(FileSystemItem item:lists){
            if(item.isFolder()){
                System.out.println(indentSpaces+"+"+item.getName());
            }else{
                System.out.println(indentSpaces + item.getName());
            }
        }
    }
    public void openAll(int space){
        String indentSpaces = " ".repeat(space);
        System.out.println(indentSpaces + "+ " + name);
        for(FileSystemItem item:lists){
            item.openAll(space+4);
        }
    }
    public FileSystemItem cd(String name){
        for(FileSystemItem item:lists){
            if(item.getName()==name){
                return item;
            }
        }
        return null;
    }
    
    public boolean isFolder(){
        return true;
    }
}
    

class Main {
    public static void main(String[] args) {
        Folder root = new Folder("root");
        root.add(new File("file1.txt", 1));
        root.add(new File("file2.txt", 1));

        Folder docs = new Folder("docs");
        docs.add(new File("resume.pdf", 1));
        docs.add(new File("notes.txt", 1));
        root.add(docs);
        // root.ls(0);
        
        root.openAll(0);
        System.out.println(root.getSize());
        
    }
}
