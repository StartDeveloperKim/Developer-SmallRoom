const programmingLanguages = [
    "JavaScript",
    "Python",
    "Java",
    "C++",
    "C#",
    "Ruby",
    "Go",
    "Swift",
    "Kotlin",
    "TypeScript",
    "Rust",
    "PHP",
    "Perl",
    "HTML",
    "CSS"
];


const backendTechStack = [
    'Node.js',
    'Express',
    'Ruby on Rails',
    'Django',
    'ASP.NET',
    'Spring Boot',
    'PHP',
    'Flask',
    'Laravel',
];

const frontendTechStack = [
    'HTML',
    'CSS',
    'JavaScript',
    'React',
    'Angular',
    'Vue.js',
    'Ember.js',
    'Svelte',
];

const infrastructureTechStack = [
    'AWS',
    'Google Cloud Platform',
    'Azure',
    'Docker',
    'Kubernetes',
    'Nginx',
    'Apache',
    'Redis',
    'MongoDB',
    'PostgreSQL',
];

const allTechStack = [...programmingLanguages, ...backendTechStack, ...frontendTechStack, ...infrastructureTechStack];

export const data = {techStack: allTechStack};
