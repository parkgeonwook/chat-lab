const startPanel = document.getElementById("start-panel");
const chatPanel = document.getElementById("chat-panel");
const startForm = document.getElementById("start-form");
const composerForm = document.getElementById("composer-form");
const restartButton = document.getElementById("restart-button");
const myNameInput = document.getElementById("my-name");
const partnerNameInput = document.getElementById("partner-name");
const messageInput = document.getElementById("message-input");
const messageList = document.getElementById("message-list");
const chatTitle = document.getElementById("chat-title");
const emptyState = document.getElementById("empty-state");

let currentUser = "";
let partnerUser = "";

function escapeHtml(value) {
	return value
		.replaceAll("&", "&amp;")
		.replaceAll("<", "&lt;")
		.replaceAll(">", "&gt;")
		.replaceAll("\"", "&quot;")
		.replaceAll("'", "&#39;");
}

function renderMessages(messages) {
	messageList.innerHTML = "";
	emptyState.classList.toggle("is-hidden", messages.length > 0);

	messages.forEach((message) => {
		const isMe = message.sender === currentUser;
		const row = document.createElement("div");
		row.className = `message-row ${isMe ? "is-me" : "is-other"}`;

		const bubble = document.createElement("article");
		bubble.className = "message-bubble";
		bubble.innerHTML = `
			<div class="message-meta">${escapeHtml(message.sender)}</div>
			<div>${escapeHtml(message.content)}</div>
		`;

		row.appendChild(bubble);
		messageList.appendChild(row);
	});

	messageList.scrollTop = messageList.scrollHeight;
}

function openChatScreen() {
	startPanel.classList.add("is-hidden");
	chatPanel.classList.remove("is-hidden");
	chatTitle.textContent = `${partnerUser}\uC640\uC758 \uB300\uD654`;
	renderMessages([]);
	messageInput.focus();
}

function resetChat() {
	chatPanel.classList.add("is-hidden");
	startPanel.classList.remove("is-hidden");
	messageList.innerHTML = "";
	messageInput.value = "";
	myNameInput.focus();
}

startForm.addEventListener("submit", (event) => {
	event.preventDefault();

	currentUser = myNameInput.value.trim();
	partnerUser = partnerNameInput.value.trim();

	if (!currentUser || !partnerUser) {
		return;
	}

	openChatScreen();
});

composerForm.addEventListener("submit", (event) => {
	event.preventDefault();
	const content = messageInput.value.trim();

	if (!content) {
		return;
	}

	messageInput.value = "";
	messageInput.focus();
});

restartButton.addEventListener("click", () => {
	resetChat();
});
