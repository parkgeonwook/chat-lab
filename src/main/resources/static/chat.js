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
const statusMessage = document.getElementById("status-message");

let currentUser = "";
let partnerUser = "";
let stompClient = null;

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

function appendMessage(message) {
	emptyState.classList.add("is-hidden");

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
	messageList.scrollTop = messageList.scrollHeight;
}

function setStatusMessage(message) {
	statusMessage.textContent = message;
	statusMessage.classList.toggle("is-hidden", !message);
}

async function fetchConversation() {
	const params = new URLSearchParams({
		currentUser,
		partnerUser
	});

	const response = await fetch(`/api/messages/conversation?${params.toString()}`);

	if (!response.ok) {
		throw new Error(`conversation request failed: ${response.status}`);
	}

	return response.json();
}

function connectWebSocket() {
	if (stompClient?.connected) {
		return;
	}

	const socket = new SockJS("/ws");
	stompClient = Stomp.over(socket);
	stompClient.debug = () => {};

	stompClient.connect({}, () => {
		subscribeToMyQueue();
		setStatusMessage("");
	});
}

function subscribeToMyQueue() {
	stompClient.subscribe(`/queue/messages/${currentUser}`, (messageFrame) => {
		const message = JSON.parse(messageFrame.body);
		appendMessage(message);
	});
}

async function openChatScreen() {
	startPanel.classList.add("is-hidden");
	chatPanel.classList.remove("is-hidden");
	chatTitle.textContent = `${partnerUser}\uC640\uC758 \uB300\uD654`;
	setStatusMessage("\uC774\uC804 \uB300\uD654\uB97C \uBD88\uB7EC\uC624\uB294 \uC911\uC785\uB2C8\uB2E4.");
	renderMessages([]);

	try {
		const messages = await fetchConversation();
		renderMessages(messages);
		setStatusMessage("\uC2E4\uC2DC\uAC04 \uC5F0\uACB0 \uC900\uBE44 \uC911\uC785\uB2C8\uB2E4.");
		connectWebSocket();
	} catch (error) {
		console.error(error);
		setStatusMessage("\uC774\uC804 \uB300\uD654\uB97C \uBD88\uB7EC\uC624\uC9C0 \uBABB\uD588\uC2B5\uB2C8\uB2E4.");
	}

	messageInput.focus();
}

function resetChat() {
	if (stompClient?.connected) {
		stompClient.disconnect(() => {});
	}

	stompClient = null;
	chatPanel.classList.add("is-hidden");
	startPanel.classList.remove("is-hidden");
	messageList.innerHTML = "";
	messageInput.value = "";
	setStatusMessage("");
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

	if (!content || !stompClient?.connected) {
		return;
	}

	const message = {
		sender: currentUser,
		receiver: partnerUser,
		content
	};

	stompClient.send("/app/chat/send", {}, JSON.stringify(message));
	appendMessage(message);
	messageInput.value = "";
	messageInput.focus();
});

restartButton.addEventListener("click", () => {
	resetChat();
});
