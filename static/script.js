document.addEventListener('DOMContentLoaded', function () {

    const form = document.getElementById('uploadForm');
    const resultDiv = document.getElementById('result');

    const score = document.getElementById('score');
    const level = document.getElementById('level');
    const keywords = document.getElementById('keywords');

    form.addEventListener('submit', function (event) {
        event.preventDefault();

        const pdfFile = document.getElementById('pdfUpload').files[0];
        const jobDescription = document.getElementById('jobDescription').value;

        console.log('PDF File:', pdfFile);
        console.log('Job Description:', jobDescription);

        // Validation
        if (!pdfFile || pdfFile.type !== 'application/pdf') {
            alert('Please upload a valid PDF file.');
            return;
        }

        if (jobDescription.trim() === '') {
            alert('Job description cannot be empty.');
            return;
        }

        // Show loading
        resultDiv.style.display = "block";
        score.innerText = "Analyzing...";
        level.innerText = "";
        keywords.innerText = "";

        console.log("Loading...");

        setTimeout(() => {

            score.innerText = "85%";
            level.innerText = "Good Match";
            keywords.innerText = "AI, Resume, Job";

            console.log("Score displayed");

        }, 1000);

    });

});