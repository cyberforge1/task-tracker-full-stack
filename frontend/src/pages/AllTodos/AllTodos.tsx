// AllTodos.tsx

import React, { useEffect, useState } from "react";
import { deleteTodo, updateTodo } from "../../services/todo-services";
import TodoCard from "../../components/TodoCard/TodoCard";
import { Todo } from "../../types/types";
import "./AllTodos.scss";

interface AllTodosProps {
  todos: Todo[];
  setTodos: React.Dispatch<React.SetStateAction<Todo[]>>;
  filter: string;
}

const AllTodos: React.FC<AllTodosProps> = ({ todos, setTodos, filter }) => {
  const [filteredTodos, setFilteredTodos] = useState<Todo[]>([]);

  useEffect(() => {
    let filtered: Todo[] = [];
    if (filter === "completed") {
      filtered = todos.filter(todo => todo.completed);
    } else if (filter === "incomplete") {
      filtered = todos.filter(todo => !todo.completed);
    } else {
      filtered = todos;
    }

    // Sort by creation date in descending order (most recent first)
    filtered.sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime());

    setFilteredTodos(filtered);
  }, [todos, filter]);

  const handleDelete = (id: number) => {
    deleteTodo(id)
      .then(() => setTodos(todos.filter(todo => todo.id !== id)))
      .catch((e) => console.error(e));
  };

  const handleUpdate = (id: number, data: Partial<Todo>) => {
    updateTodo(id, data)
      .then((updatedTodo) => setTodos(todos.map(todo => (todo.id === id ? updatedTodo : todo))))
      .catch((e) => console.error(e));
  };

  const incompleteTodos = filteredTodos.filter(todo => !todo.completed);
  const completedTodos = filteredTodos.filter(todo => todo.completed);

  return (
    <div className="todo-list">
      {incompleteTodos.map((todo) => (
        <TodoCard key={todo.id} todo={todo} onDelete={handleDelete} onUpdate={handleUpdate} />
      ))}
      {completedTodos.map((todo) => (
        <TodoCard key={todo.id} todo={todo} onDelete={handleDelete} onUpdate={handleUpdate} />
      ))}
    </div>
  );
};

export default AllTodos;
