// AJAX: при выборе университета загружаем его общежития из /api/dormitories и заполняем второй список
const university = document.getElementById("university");
const dormitory = document.getElementById("dormitory");

function resetDormitories(text) {
    dormitory.innerHTML = "";
    const option = document.createElement("option");
    option.value = "";
    option.textContent = text;
    dormitory.appendChild(option);
}

university.addEventListener("change", () => {
    const universityId = university.value;

    if (universityId === "") {
        resetDormitories("— сначала выберите университет —");
        dormitory.disabled = true;
        return;
    }

    // Адрес API берём из data-url: в нём уже есть contextPath, в JS его не хардкодим
    fetch(university.dataset.url + "?universityId=" + encodeURIComponent(universityId))
        .then(response => {
            if (!response.ok) {
                throw new Error("HTTP " + response.status);
            }
            return response.json();
        })
        .then(dormitories => {
            resetDormitories("— выберите общежитие —");
            dormitories.forEach(d => {
                const option = document.createElement("option");
                option.value = d.id;
                // textContent, а не innerHTML: название вставляется как текст (защита от XSS)
                option.textContent = d.name;
                dormitory.appendChild(option);
            });
            dormitory.disabled = false;
        })
        .catch(() => {
            resetDormitories("Не удалось загрузить общежития");
            dormitory.disabled = true;
        });
});
