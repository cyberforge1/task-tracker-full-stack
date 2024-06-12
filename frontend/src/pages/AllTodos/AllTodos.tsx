// AllTodos.tsx

import React, { useEffect, useState } from "react";
import { deleteTodo, updateTodo } from "../../services/todo-services";
import TodoCard from "../../components/TodoCard/TodoCard";
import TodoTile from "../../components/TodoTile/TodoTile";
import { Todo } from "../../types/types";
import "./AllTodos.scss";

interface AllTodosProps {
  todos: Todo[];
  setTodos: React.Dispatch<React.SetStateAction<Todo[]>>;
  filter: string;
  viewMode: string; 
}

const AllTodos: React.FC<AllTodosProps> = ({ todos, setTodos, filter, viewMode }) => {
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

  return (
    <div className={`todo-list ${viewMode === 'tile' ? 'tile' : 'card'}`}>
      {viewMode === "tile" ? (
        <div className="todo-tiles-wrapper">
          {filteredTodos.map((todo) => (
            <TodoTile key={todo.id} todo={todo} onDelete={handleDelete} onUpdate={handleUpdate} />
          ))}
        </div>
      ) : (
        filteredTodos.map((todo) => (
          <TodoCard key={todo.id} todo={todo} onDelete={handleDelete} onUpdate={handleUpdate} />
        ))
      )}
    </div>
  );
};

export default AllTodos;
