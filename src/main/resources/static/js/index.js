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

let searchWord = [];

const tag_input = document.getElementById("default-search");
let tagify = new Tagify(tag_input, {
    whitelist: allTechStack,
    dropdown: {
        classname: "tags-look",
        enabled: 0,
        closeOnSelect: false
    },
}); // initialize Tagify
// 태그가 추가되면 이벤트 발생

// 태그가 추가될 떄 마다 AJAX 통신을 활용해서 태그 기반 검색을 하자
tagify.on('add', function (e) {
    const tags = e.detail.tagify.value.map(tag => tag.value);
    searchWord=[...tags]
    console.log('추가 전체 검색어:', searchWord);
});

tagify.on('remove', function (e) {
    const tags = e.detail.tagify.value.map(tag => tag.value);
    searchWord=[...tags]

    console.log('삭제 전체 검색어:', searchWord);
});