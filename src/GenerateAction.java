import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.List;

/**
 * Created by chathurangi on 7/16/17.
 */
public class GenerateAction extends AnAction {


    public void actionPerformed(AnActionEvent e) {
        PsiClass psiClass = getPsiClassFRomContext(e);
        GenerateDialog dlg=new GenerateDialog(psiClass);
        dlg.show();
        if(dlg.isOK()){
            generateCompareTo(psiClass, dlg.getFields());
        }
    }

    private void generateCompareTo(PsiClass psiClass, List<PsiField> fields) {
        new WriteCommandAction.Simple(psiClass.getProject(),psiClass.getContainingFile()){

            @Override
            protected void run() throws Throwable {
                StringBuilder builder = new StringBuilder("public int compareTo(");
                builder.append(psiClass.getName()).append(" that) {\n");
                builder.append("return com.google.common.collect.ComparisonChain.start()");
                for(PsiField field : fields){
                    builder.append(".compare(this.").append(field.getName()).append(", that.");
                    builder.append(field.getName()).append(")");
                }
                builder.append(".result();\n");
                PsiElementFactory elementFactory=JavaPsiFacade.getElementFactory(getProject());
                PsiMethod compareTo=elementFactory.createMethodFromText(builder.toString(),psiClass);
                PsiElement method = psiClass.add(compareTo);
                JavaCodeStyleManager.getInstance(getProject()).shortenClassReferences(method);


            }
        }.execute();
    }

    @Override
    public void update(AnActionEvent e) {
        PsiClass psiClass = getPsiClassFRomContext(e);
        e.getPresentation().setEnabled(psiClass!=null);
    }



    private PsiClass getPsiClassFRomContext(AnActionEvent e) {

        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        if (psiFile == null || editor == null) {

           return null;

        }
        int offset = editor.getCaretModel().getOffset();
        PsiElement elementAt = psiFile.findElementAt(offset);
        PsiClass psiClass = PsiTreeUtil.getParentOfType(elementAt, PsiClass.class);
        return psiClass;

    }
}
