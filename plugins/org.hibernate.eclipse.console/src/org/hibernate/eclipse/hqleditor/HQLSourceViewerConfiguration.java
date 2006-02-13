package org.hibernate.eclipse.hqleditor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.formatter.ContentFormatter;
import org.eclipse.jface.text.formatter.IContentFormatter;
import org.eclipse.jface.text.formatter.IFormattingStrategy;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.BufferedRuleBasedScanner;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

public class HQLSourceViewerConfiguration extends SourceViewerConfiguration {

	public final static String HQL_PARTITIONING= "__hql_partitioning";   //$NON-NLS-1$
	private HQLCompletionProcessor completionProcessor;
	private HQLEditor hqlEditor;
    
    static class SingleTokenScanner extends BufferedRuleBasedScanner {
        public SingleTokenScanner( TextAttribute attribute ) {
            setDefaultReturnToken( new Token( attribute ));
        }
    }

    public HQLSourceViewerConfiguration(HQLEditor editor) {
    	hqlEditor = editor;
        completionProcessor = new HQLCompletionProcessor(editor); 
    }
    
    public HQLEditor getHQLEditor() {
    	return hqlEditor;
    }
       
    public String getConfiguredDocumentPartitioning( ISourceViewer sourceViewer ) {
        return HQL_PARTITIONING;
    }

    public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
        ContentAssistant assistant = new ContentAssistant();
    
        assistant.setDocumentPartitioning( getConfiguredDocumentPartitioning( sourceViewer ));
        
        completionProcessor = new HQLCompletionProcessor(hqlEditor);
        assistant.setContentAssistProcessor( completionProcessor, IDocument.DEFAULT_CONTENT_TYPE);
        
        assistant.enableAutoActivation( true );
        assistant.setAutoActivationDelay( 500 );
        assistant.setProposalPopupOrientation( IContentAssistant.PROPOSAL_STACKED );        
        
        return assistant;
    }

    public IContentFormatter getContentFormatter(ISourceViewer sourceViewer) {
        ContentFormatter formatter = new ContentFormatter();
        formatter.setDocumentPartitioning( HQL_PARTITIONING );
        
        IFormattingStrategy formattingStrategy = new HQLFormattingStrategy();
        formatter.setFormattingStrategy( formattingStrategy, IDocument.DEFAULT_CONTENT_TYPE );
                       
        return formatter;
    }

 
    public IPresentationReconciler getPresentationReconciler( ISourceViewer sourceViewer ) {

        HQLColors colorProvider = new HQLColors();
        
        PresentationReconciler reconciler = new PresentationReconciler();
        String docPartitioning = getConfiguredDocumentPartitioning( sourceViewer );
        reconciler.setDocumentPartitioning( docPartitioning );

        DefaultDamagerRepairer dr = new DefaultDamagerRepairer( new HQLCodeScanner( colorProvider ) );
        reconciler.setDamager( dr, IDocument.DEFAULT_CONTENT_TYPE );
        reconciler.setRepairer( dr, IDocument.DEFAULT_CONTENT_TYPE );
        
        dr = new DefaultDamagerRepairer( new SingleTokenScanner( new TextAttribute( colorProvider.getColor( HQLColors.HQL_COMMENT_COLOR ))));
        reconciler.setDamager( dr, HQLPartitionScanner.HQL_COMMENT );
        reconciler.setRepairer( dr, HQLPartitionScanner.HQL_COMMENT );

        dr = new DefaultDamagerRepairer( new SingleTokenScanner( new TextAttribute( colorProvider.getColor( HQLColors.HQL_QUOTED_LITERAL_COLOR ))));
        reconciler.setDamager( dr, HQLPartitionScanner.HQL_QUOTED_LITERAL );
        reconciler.setRepairer( dr, HQLPartitionScanner.HQL_QUOTED_LITERAL );

        return reconciler;
    }

    
    public ITextHover getTextHover( ISourceViewer sourceViewer, String contentType ) {
        return new HQLTextHover();
    }
   
} 
