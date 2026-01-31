package com.vendrconnect.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Knowledge Base Service for RAG (Retrieval-Augmented Generation).
 * This service manages the system knowledge base containing platform information,
 * FAQs, and documentation. It provides methods to search and retrieve
 * relevant knowledge snippets based on user queries.
 * 
 * For production, this should be replaced with a proper vector database
 * like Pinecone, Weaviate, or Milvus with actual embeddings.
 * 
 * @author VenderConnect Team
 * @version 2.0 (RAG Implementation)
 */
@Service
public class KnowledgeBaseService {

    private static final Logger logger = LoggerFactory.getLogger(KnowledgeBaseService.class);
    
    /**
     * In-memory knowledge base with documents containing system information.
     * Each entry has content, category, and keywords for semantic search.
     * In production, replace this with vector database queries.
     */
    private final List<KnowledgeDocument> knowledgeBase;
    
    /**
     * Constructor initializes the knowledge base with platform documentation.
     * Documents cover various aspects: registration, jobs, payments, etc.
     */
    public KnowledgeBaseService() {
        this.knowledgeBase = initializeKnowledgeBase();
        logger.info("Knowledge Base initialized with {} documents", knowledgeBase.size());
    }
    
    /**
     * Initialize knowledge base with platform documentation and FAQs.
     * This is a simplified version. In production, load from database files.
     * 
     * @return List of knowledge documents
     */
    private List<KnowledgeDocument> initializeKnowledgeBase() {
        List<KnowledgeDocument> docs = new ArrayList<>();
        
        // User Registration & Authentication
        docs.add(new KnowledgeDocument(
            "user-registration",
            Arrays.asList("register", "signup", "account", "create", "sign up"),
            "Users can register on VenderConnect by clicking 'Sign Up' on the homepage. " +
            "Choose between User (to post jobs) or Vendor (to offer services) account types. " +
            "Registration requires email, name, location, and password. " +
            "Users can also sign up with Google OAuth for quick access."
        ));
        
        docs.add(new KnowledgeDocument(
            "google-auth",
            Arrays.asList("google", "oauth", "social login", "sign in"),
            "VenderConnect supports Google OAuth authentication. Click 'Sign in with Google' " +
            "to quickly create an account or log in. Your Google profile information " +
            "will be used to pre-fill your account details."
        ));
        
        // Job Posting for Users
        docs.add(new KnowledgeDocument(
            "post-job",
            Arrays.asList("post job", "create job", "service request", "find vendor"),
            "To post a job: 1) Log in as User, 2) Click 'Post Job' button, " +
            "3) Fill in job details (title, description, category, location, budget), " +
            "4) Select urgency level, 5) Submit. Vendors in your area will see " +
            "your job and can send proposals."
        ));
        
        docs.add(new KnowledgeDocument(
            "job-categories",
            Arrays.asList("categories", "services", "types", "available"),
            "Available service categories on VenderConnect: Plumbing, Electrical, Carpentry, " +
            "Painting, Cleaning, HVAC, Landscaping, Moving, and more. " +
            "When posting a job, select the most relevant category for better vendor matching."
        ));
        
        docs.add(new KnowledgeDocument(
            "job-budget",
            Arrays.asList("budget", "price", "cost", "payment", "money"),
            "When posting a job, specify a budget range (minimum and maximum). " +
            "This helps vendors understand your price expectations. " +
            "Budgets can be modified later if needed. Final payment is made " +
            "after job completion to vendor satisfaction."
        ));
        
        docs.add(new KnowledgeDocument(
            "job-status",
            Arrays.asList("status", "track", "progress", "completed", "pending"),
            "Jobs have several statuses: Pending (not yet accepted), " +
            "Accepted (vendor assigned), In Progress (work ongoing), " +
            "Completed (work finished). Users and vendors can track status " +
            "in their dashboards. Vendors must update status as they progress."
        ));
        
        // Vendor Operations
        docs.add(new KnowledgeDocument(
            "vendor-registration",
            Arrays.asList("vendor", "register", "offer services", "become provider"),
            "To register as a vendor: 1) Click 'Become Vendor', 2) Fill business details " +
            "(name, email, location), 3) Select service categories you offer, " +
            "4) Add team members if applicable, 5) Complete profile. " +
            "You'll start seeing available jobs matching your services."
        ));
        
        docs.add(new KnowledgeDocument(
            "accept-job",
            Arrays.asList("accept", "take job", "respond", "proposal"),
            "Vendors can browse available jobs and accept ones matching their skills. " +
            "To accept: 1) View job details, 2) Click 'Accept Job', " +
            "3) Confirm. The customer will be notified and you'll be connected. " +
            "Only accept jobs you're confident in completing successfully."
        ));
        
        docs.add(new KnowledgeDocument(
            "vendor-availability",
            Arrays.asList("available", "status", "toggle", "busy"),
            "Vendors can set their availability status: Available, Busy, or Offline. " +
            "When Available, you'll receive job notifications and appear in searches. " +
            "Set to Busy when working on other jobs, and Offline when not seeking work."
        ));
        
        docs.add(new KnowledgeDocument(
            "vendor-categories",
            Arrays.asList("services", "offerings", "skills", "add category"),
            "Vendors can offer multiple service categories. During registration or profile edit, " +
            "select all categories you provide services in. Common categories include: " +
            "Plumbing, Electrical, Carpentry, Painting, Cleaning, HVAC, Landscaping. " +
            "More categories mean more job opportunities."
        ));
        
        // Team Management
        docs.add(new KnowledgeDocument(
            "team-members",
            Arrays.asList("team", "members", "employees", "workers"),
            "Vendors can add team members to help manage larger jobs. " +
            "In your Vendor Dashboard, go to Team section and add member details " +
            "(name, email, role). Team members can be assigned to specific jobs " +
            "for better coordination and workload distribution."
        ));
        
        // Profile Management
        docs.add(new KnowledgeDocument(
            "edit-profile",
            Arrays.asList("profile", "edit", "update", "change details"),
            "To edit your profile: Click on your profile picture or name in the header, " +
            "select 'Edit Profile'. You can update name, location, phone number, " +
            "profile image, and service categories (for vendors). Changes save automatically."
        ));
        
        docs.add(new KnowledgeDocument(
            "profile-image",
            Arrays.asList("photo", "picture", "avatar", "upload image"),
            "Upload a profile picture to build trust with customers/vendors. " +
            "In Edit Profile, click 'Upload Image', select a file " +
            "(max 5MB, recommended JPG/PNG), and confirm. " +
            "Profile images help verify identity and create professional presence."
        ));
        
        // Payment & Security
        docs.add(new KnowledgeDocument(
            "payments",
            Arrays.asList("pay", "payment", "transaction", "money transfer"),
            "Payments on VenderConnect are processed securely after job completion. " +
            "Users pay the agreed budget amount. Funds are held securely " +
            "and released to vendor when both parties confirm completion. " +
            "Always pay through the platform - never directly to vendors."
        ));
        
        docs.add(new KnowledgeDocument(
            "disputes",
            Arrays.asList("dispute", "problem", "issue", "complaint", "refund"),
            "If you have issues with a job or vendor, contact support through the chatbot " +
            "or email. VenderConnect mediates disputes and can arrange refunds " +
            "if terms weren't met. Document all communications for evidence."
        ));
        
        // Finding Help
        docs.add(new KnowledgeDocument(
            "contact-support",
            Arrays.asList("help", "support", "contact", "email", "assistance"),
            "Need help? Contact VenderConnect support through: 1) Chat with our AI assistant " +
            "(bottom right corner), 2) Email support@vendrconnect.com, " +
            "3) Use the 'Report Issue' feature in your dashboard. " +
            "Response time: Usually within 24 hours for urgent issues."
        ));
        
        docs.add(new KnowledgeDocument(
            "faq-troubleshooting",
            Arrays.asList("problem", "issue", "not working", "error", "trouble"),
            "Common issues: Can't log in? Check credentials or reset password. " +
            "Job not appearing? Check status filters. Payment failed? Ensure card is valid. " +
            "Vendor not responding? Try another vendor. For persistent issues, contact support."
        ));
        
        return docs;
    }
    
    /**
     * Search knowledge base for relevant documents based on user query.
     * Uses keyword matching for similarity. In production, use vector embeddings
     * and cosine similarity for semantic search.
     * 
     * @param query User's question or search term
     * @return List of relevant knowledge documents sorted by relevance
     */
    public List<KnowledgeDocument> searchKnowledge(String query) {
        if (query == null || query.trim().isEmpty()) {
            return Collections.emptyList();
        }
        
        logger.debug("Searching knowledge base for: {}", query);
        String lowerQuery = query.toLowerCase();
        
        /**
         * Score documents based on keyword matches.
         * Higher score = more relevant to query.
         */
        List<ScoredDocument> scoredDocs = new ArrayList<>();
        
        for (KnowledgeDocument doc : knowledgeBase) {
            int score = calculateRelevanceScore(doc, lowerQuery);
            if (score > 0) {
                scoredDocs.add(new ScoredDocument(doc, score));
            }
        }
        
        /**
         * Sort by score (highest first) and return top 3 results.
         * Top results provide context for RAG-enhanced AI responses.
         */
        scoredDocs.sort((a, b) -> Integer.compare(b.score, a.score));
        
        List<KnowledgeDocument> results = new ArrayList<>();
        for (int i = 0; i < Math.min(3, scoredDocs.size()); i++) {
            results.add(scoredDocs.get(i).document);
        }
        
        logger.debug("Found {} relevant documents for query", results.size());
        return results;
    }
    
    /**
     * Calculate relevance score between document and query using keyword matching.
     * Simple implementation for demonstration. In production, use embedding similarity.
     * 
     * @param doc Knowledge document to score
     * @param query Lowercase search query
     * @return Relevance score (higher = more relevant)
     */
    private int calculateRelevanceScore(KnowledgeDocument doc, String query) {
        String[] queryWords = query.split("\\s+");
        int score = 0;
        
        for (String word : queryWords) {
            if (word.length() < 3) continue; // Skip very short words
            
            for (String keyword : doc.getKeywords()) {
                if (keyword.toLowerCase().contains(word) || 
                    word.contains(keyword.toLowerCase())) {
                    score += 10; // Exact/partial keyword match
                }
            }
        }
        
        // Bonus for matches in content
        for (String word : queryWords) {
            if (word.length() < 3) continue;
            if (doc.getContent().toLowerCase().contains(word)) {
                score += 5; // Content match
            }
        }
        
        return score;
    }
    
    /**
     * Build RAG context string from relevant documents.
     * Combines content from top search results into a formatted context
     * for the AI to reference when generating responses.
     * 
     * @param query User's question
     * @return Formatted context string with relevant knowledge
     */
    public String buildRagContext(String query) {
        List<KnowledgeDocument> relevantDocs = searchKnowledge(query);
        
        if (relevantDocs.isEmpty()) {
            return "";
        }
        
        StringBuilder context = new StringBuilder();
        context.append("\n\n=== RELEVANT KNOWLEDGE BASE ===\n");
        
        for (int i = 0; i < relevantDocs.size(); i++) {
            KnowledgeDocument doc = relevantDocs.get(i);
            context.append(String.format("\n[%d] %s\n%s\n", 
                i + 1, doc.getCategory(), doc.getContent()));
        }
        
        context.append("\n=== END KNOWLEDGE BASE ===\n\n");
        
        logger.info("Built RAG context from {} documents", relevantDocs.size());
        return context.toString();
    }
    
    /**
     * Inner class representing a knowledge document.
     * Contains content, category, and keywords for search.
     */
    public static class KnowledgeDocument {
        private final String category;
        private final List<String> keywords;
        private final String content;
        
        public KnowledgeDocument(String category, List<String> keywords, String content) {
            this.category = category;
            this.keywords = keywords;
            this.content = content;
        }
        
        public String getCategory() { return category; }
        public List<String> getKeywords() { return keywords; }
        public String getContent() { return content; }
    }
    
    /**
     * Inner class for scoring documents during search.
     * Used internally for sorting by relevance.
     */
    private static class ScoredDocument {
        final KnowledgeDocument document;
        final int score;
        
        public ScoredDocument(KnowledgeDocument document, int score) {
            this.document = document;
            this.score = score;
        }
    }
}
