package com.quocanh.doan.config;

import com.quocanh.doan.Model.JobCategory;
import com.quocanh.doan.Model.JobField;
import com.quocanh.doan.Model.Skill;
import com.quocanh.doan.Repository.JobCategoryRepository;
import com.quocanh.doan.Repository.JobFieldRepository;
import com.quocanh.doan.Repository.SkillRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@AllArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private JobCategoryRepository jobCategoryRepository;
    private SkillRepository skillRepository;
    private JobFieldRepository jobFieldRepository;

    private static final Map<String, Map<String, List<String>>> CATEGORY_DATA = new LinkedHashMap<>() {{
        put("Accounting & Consulting", new HashMap<>() {{
            put("jobfield", Arrays.asList(
                    "Financial Analyst", "Tax Consultant", "Certified Public Accountant (CPA)", "Management Consultant",
                    "HR Specialist", "Business Coach", "Payroll Manager"
            ));
            put("skills", Arrays.asList(
                    "Financial Analysis", "Tax Preparation", "Auditing", "Budget Management", "Cost Accounting",
                    "Financial Reporting", "Risk Management", "Business Valuation", "Payroll Management", "QuickBooks",
                    "Management Consulting", "Strategic Planning", "Process Improvement", "Change Management", "Leadership Coaching"
            ));
        }});
        put("Admin Support", new HashMap<>() {{
            put("jobfield", Arrays.asList(
                    "Virtual Assistant", "Data Entry Clerk", "Project Coordinator", "Administrative Assistant",
                    "Office Manager", "Customer Support Representative"
            ));
            put("skills", Arrays.asList(
                    "Data Entry", "Calendar Management", "Email Management", "Virtual Assistance", "Office Management",
                    "Document Preparation", "Travel Coordination", "Meeting Scheduling", "Transcription", "Customer Support",
                    "File Organization", "Time Management", "Microsoft Office Suite", "Google Workspace", "CRM Management"
            ));
        }});
        put("Customer Service", new HashMap<>() {{
            put("jobfield", Arrays.asList(
                    "Technical Support Specialist", "Customer Service Representative", "Call Center Agent",
                    "Client Success Manager", "Support Team Lead"
            ));
            put("skills", Arrays.asList(
                    "Technical Support", "Call Center Operations", "Complaint Resolution", "Customer Relationship Management",
                    "Live Chat Support", "Ticketing Systems", "Empathy and Active Listening", "Conflict Resolution", "Multitasking",
                    "Phone Etiquette", "Zendesk", "Salesforce Service Cloud", "Problem Solving", "Patience", "Cross-Selling"
            ));
        }});
        put("Data Science & Analytics", new HashMap<>() {{
            put("jobfield", Arrays.asList(
                    "Data Scientist", "Machine Learning Engineer", "Data Analyst", "Business Intelligence Analyst",
                    "Data Engineer", "Statistician"
            ));
            put("skills", Arrays.asList(
                    "Data Analysis", "Machine Learning", "Data Visualization", "Statistical Modeling", "R Programming",
                    "Tableau", "Power BI", "Big Data", "Hadoop", "Apache Spark", "Predictive Analytics", "Data Mining", "A/B Testing"
            ));
        }});
        put("Design & Creative", new HashMap<>() {{
            put("jobfield", Arrays.asList(
                    "Graphic Designer", "UI/UX Designer", "Video Editor", "Illustrator", "Creative Director",
                    "Motion Graphics Designer"
            ));
            put("skills", Arrays.asList(
                    "Graphic Design", "UI/UX Design", "Web Design", "Adobe Photoshop", "Adobe Illustrator", "Figma", "Sketch",
                    "Video Editing", "Motion Graphics", "Adobe Premiere Pro", "After Effects", "Photography", "Illustration", "Branding", "Typography"
            ));
        }});
        put("Engineering & Architecture", new HashMap<>() {{
            put("jobfield", Arrays.asList(
                    "Civil Engineer", "Mechanical Engineer", "Architect", "Electrical Engineer", "Structural Engineer",
                    "Project Engineer"
            ));
            put("skills", Arrays.asList(
                    "AutoCAD", "Revit", "Civil Engineering", "Mechanical Engineering", "Electrical Engineering", "Structural Analysis",
                    "Project Engineering", "3D Modeling", "SolidWorks", "MATLAB", "HVAC Design", "Piping Design", "Construction Management",
                    "Blueprint Reading", "Finite Element Analysis"
            ));
        }});
        put("IT & Networking", new HashMap<>() {{
            put("jobfield", Arrays.asList(
                    "System Administrator", "Network Engineer", "Cybersecurity Analyst", "DevOps Engineer",
                    "Cloud Architect", "IT Support Specialist"
            ));
            put("skills", Arrays.asList(
                    "System Administration", "Network Security", "Cloud Computing", "AWS", "Azure", "Google Cloud Platform", "DevOps",
                    "Docker", "Kubernetes", "Linux Administration", "Windows Server", "Cybersecurity", "Penetration Testing", "IT Support",
                    "Network Configuration"
            ));
        }});
        put("Legal", new HashMap<>() {{
            put("jobfield", Arrays.asList(
                    "Corporate Lawyer", "Contract Specialist", "Intellectual Property Attorney", "Legal Researcher",
                    "Compliance Officer", "Paralegal"
            ));
            put("skills", Arrays.asList(
                    "Contract Drafting", "Legal Research", "Litigation Support", "Intellectual Property Law", "Corporate Law",
                    "Compliance Management", "Mediation", "Arbitration", "Legal Writing", "Case Management", "Regulatory Affairs",
                    "Due Diligence", "Employment Law", "Tax Law", "Patent Filing"
            ));
        }});
        put("Sales & Marketing", new HashMap<>() {{
            put("jobfield", Arrays.asList(
                    "Digital Marketing Specialist", "SEO Expert", "Content Marketer", "Sales Manager",
                    "Brand Manager", "Marketing Analyst"
            ));
            put("skills", Arrays.asList(
                    "Digital Marketing", "SEO (Search Engine Optimization)", "SEM (Search Engine Marketing)", "Content Marketing",
                    "Social Media Marketing", "Email Marketing", "Google Analytics", "Google Ads", "Facebook Ads", "Sales Strategy",
                    "Lead Generation", "Cold Calling", "Negotiation", "Market Research", "Brand Management"
            ));
        }});
        put("Programming & Development", new HashMap<>() {{
            put("jobfield", Arrays.asList(
                    "Frontend Developer", "Backend Developer", "Full Stack Developer", "Software Engineer",
                    "Web Developer", "Mobile App Developer"
            ));
            put("skills", Arrays.asList(
                    "JavaScript", "React", "Next.js", "Node.js", "Python", "Java", "C++", "C#", "Ruby", "PHP", "HTML", "CSS",
                    "TypeScript", "SQL", "MongoDB"
            ));
        }});
        put("Soft Skills", new HashMap<>() {{
            put("jobfield", Arrays.asList(
                    "Team Leader", "Motivational Speaker", "Career Coach", "Mediator", "Facilitator"
            ));
            put("skills", Arrays.asList(
                    "Communication", "Teamwork", "Problem Solving", "Time Management", "Adaptability", "Leadership", "Critical Thinking",
                    "Emotional Intelligence", "Creativity", "Attention to Detail", "Work Ethic", "Collaboration", "Decision Making",
                    "Stress Management", "Conflict Resolution"
            ));
        }});
        put("Other Miscellaneous", new HashMap<>() {{
            put("jobfield", Arrays.asList(
                    "Project Manager", "Event Planner", "Logistics Coordinator", "Supply Chain Analyst", "Educator"
            ));
            put("skills", Arrays.asList(
                    "Project Management", "Agile Methodology", "Scrum", "Kanban", "Public Speaking", "Writing", "Editing", "Translation",
                    "Teaching", "Training", "Event Planning", "Fundraising", "Grant Writing", "Logistics", "Supply Chain Management"
            ));
        }});
    }};

    @Override
    public void run(String... args) throws Exception {
        if (jobCategoryRepository.count() == 0) {
            List<JobCategory> categories = new ArrayList<>();
            for (String categoryName : CATEGORY_DATA.keySet()) {
                JobCategory category = new JobCategory(categoryName);
                category.setStatus(JobCategory.getStatusActive());
                categories.add(category);
            }
            jobCategoryRepository.saveAll(categories);
            System.out.println("Seeded " + categories.size() + " job categories.");
        }

        if (jobFieldRepository.count() == 0) {
            List<JobCategory> categories = jobCategoryRepository.findAll();
            Map<String, JobCategory> categoryMap = new HashMap<>();
            for (JobCategory category : categories) {
                categoryMap.put(category.getCategory(), category);
            }

            List<JobField> jobFields = new ArrayList<>();
            for (Map.Entry<String, Map<String, List<String>>> entry : CATEGORY_DATA.entrySet()) {
                String categoryName = entry.getKey();
                JobCategory category = categoryMap.get(categoryName);
                List<String> jobFieldNames = entry.getValue().get("jobfield");
                for (String name : jobFieldNames) {
                    JobField jobField = JobField.builder()
                            .jobCategory(category)
                            .name(name)
                            .status(JobField.getStatusActive())
                            .build();
                    jobFields.add(jobField);
                }
            }
            jobFieldRepository.saveAll(jobFields);
            System.out.println("Seeded " + jobFields.size() + " job fields.");
        }

        if (skillRepository.count() == 0) {
            List<JobCategory> categories = jobCategoryRepository.findAll();
            Map<String, JobCategory> categoryMap = new HashMap<>();
            for (JobCategory category : categories) {
                categoryMap.put(category.getCategory(), category);
            }

            List<Skill> skills = new ArrayList<>();
            for (Map.Entry<String, Map<String, List<String>>> entry : CATEGORY_DATA.entrySet()) {
                String categoryName = entry.getKey();
                JobCategory category = categoryMap.get(categoryName);
                List<String> skillNames = entry.getValue().get("skills");
                for (String skillName : skillNames) {
                    Skill skill = Skill.builder().nameSkill(skillName).category(category).status(Skill.getStatusActive()).build();
                    skills.add(skill);
                }
            }
            skillRepository.saveAll(skills);
            System.out.println("Seeded " + skills.size() + " skills.");
        }
    }
}