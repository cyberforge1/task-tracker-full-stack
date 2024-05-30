import { Todo } from "../types/types";

const baseUrl = "http://localhost:8080";

export const getAllTodos = async (): Promise<Todo[]> => {
  const response = await fetch(baseUrl + "/todos");
  if (!response.ok) {
    throw new Error("Failed to fetch todos");
  }
  const data = await response.json();
  return data;
};

export const createTodo = async (todo: { title: string; description: string }): Promise<Todo> => {
  const response = await fetch(baseUrl + "/todos", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(todo),
  });
  if (!response.ok) {
    throw new Error("Failed to create todo");
  }
  const data = await response.json();
  return data;
};

export const deleteTodo = async (id: number): Promise<void> => {
  const response = await fetch(`${baseUrl}/todos/${id}`, {
    method: "DELETE",
  });
  if (!response.ok) {
    throw new Error("Failed to delete todo");
  }
};

export const updateTodo = async (id: number, data: Partial<Todo>): Promise<Todo> => {
  const response = await fetch(`${baseUrl}/todos/${id}`, {
    method: "PATCH",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  });
  if (!response.ok) {
    throw new Error("Failed to update todo");
  }
  const updatedTodo = await response.json();
  return updatedTodo;
};
