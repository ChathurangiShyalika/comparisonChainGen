import com.intellij.ide.util.DefaultPsiElementCellRenderer;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.ToolbarDecorator;

import javax.annotation.Nullable;
import javax.swing.*;
import java.util.Collection;
import java.util.List;

/**
 * Created by chathurangi on 7/18/17.
 */
public class GenerateDialog extends DialogWrapper{
    private CollectionListModel<PsiField> myFields;
    private final LabeledComponent<JPanel> mycomponent;
    public GenerateDialog(PsiClass psiClass){
        super(psiClass.getProject());

        setTitle("Select Fields for ComparisonChain");
        myFields= new CollectionListModel<PsiField>(psiClass.getAllFields());

        JList fieldList = new JList(myFields);
        fieldList.setCellRenderer(new DefaultPsiElementCellRenderer());
        ToolbarDecorator decorator=ToolbarDecorator.createDecorator(fieldList);
        decorator.disableAddAction();
        JPanel panel = decorator.createPanel();
        mycomponent=LabeledComponent.create(panel,"Fields to include in compareTo():");

        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel(){
        return mycomponent;
    }

    public List<PsiField> getFields() {
        return myFields.getItems();
    }
}
