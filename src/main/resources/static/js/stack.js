const programmingLanguages = [
    "JavaScript", "Python", "Java", "C++", "C#", "Ruby", "Go", "Swift", "Kotlin", "TypeScript", "Rust",
    "PHP", "Perl", "HTML", "CSS"
];


const backendTechStack = [
    'Node.js', 'Express', 'Ruby on Rails', 'Django', 'ASP.NET', 'Spring Boot', 'PHP', 'Flask', 'Laravel',
];

const frontendTechStack = [
    'React', 'Angular', 'Vue.js', 'Ember.js', 'Svelte', "JSP", "Thymeleaf", "Mustache"
];

const infrastructureTechStack = [
    'AWS', 'Google Cloud Platform', 'Azure', 'Docker', 'Kubernetes', 'Nginx', 'Apache', 'Redis', 'MongoDB',
    'PostgreSQL', 'Oracle', 'MySQL', 'Redis', 'Memcached'
];

const allTechStack = [...programmingLanguages, ...backendTechStack, ...frontendTechStack, ...infrastructureTechStack];

export const data = {techStack: allTechStack};
