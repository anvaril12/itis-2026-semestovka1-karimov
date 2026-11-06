"use strict";

const copyButton = document.getElementById("copy-code");
const code = document.getElementById("confirmation-code");
const copyStatus = document.getElementById("copy-status");

if (copyButton && code && navigator.clipboard) {
    copyButton.hidden = false;
    copyButton.addEventListener("click", async () => {
        try {
            await navigator.clipboard.writeText(code.textContent.trim());
            copyStatus.textContent = "Код скопирован";
        } catch {
            copyStatus.textContent = "Выделите код и скопируйте его вручную";
        }
    });
}

const confirmationForm = document.getElementById("confirmation-form");
confirmationForm?.addEventListener("submit", () => {
    const button = confirmationForm.querySelector("button[type='submit']");
    button.disabled = true;
    button.textContent = "Проверяем…";
});

window.addEventListener("pageshow", () => {
    const button = confirmationForm?.querySelector("button[type='submit']");
    if (button) {
        button.disabled = false;
        button.textContent = "Я подтвердил";
    }
});
