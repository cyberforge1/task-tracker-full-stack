import React, { useEffect, useState } from "react";
import { getAllTodos, createTodo, deleteTodo, updateTodo } from "../../services/todo-services";
import TodoCard from "../../components/TodoCard/TodoCard";
import { Todo } from "../../types/types";

const AllTodos: React.FC = () => {
  const [fetchStatus, setFetchStatus] = useState<string>("");
  const [todos, setTodos] = useState<Todo[]>([]);
  const [error, setError] = useState<Error | null>(null);

  useEffect(() => {
    setFetchStatus("LOADING");
    getAllTodos()
      .then((data) => {
        setTodos(data);
        setFetchStatus("SUCCESS");
      })
      .catch((e) => {
        setError(e);
        setFetchStatus("FAILURE");
      });
  }, []);

  const handleDelete = (id: number) => {
    deleteTodo(id)
      .then(() => setTodos(todos.filter(todo => todo.id !== id)))
      .catch((e) => setError(e));
  };

  const handleUpdate = (id: number, data: Partial<Todo>) => {
    updateTodo(id, data)
      .then((updatedTodo) => setTodos(todos.map(todo => (todo.id === id ? updatedTodo : todo))))
      .catch((e) => setError(e));
  };

  const handleCreate = () => {
    const newTodo = {
      title: "New Todo",
      description: "Description of the new todo",
    };
    createTodo(newTodo)
      .then((createdTodo) => setTodos([...todos, createdTodo]))
      .catch((e) => setError(e));
  };

  return (
    <>
      <button onClick={handleCreate}>Add Todo</button>
      {fetchStatus === "LOADING" && <p>Loading</p>}
      {fetchStatus === "SUCCESS" && todos.map((todo) => (
        <TodoCard key={todo.id} todo={todo} onDelete={handleDelete} onUpdate={handleUpdate} />
      ))}
      {fetchStatus === "FAILURE" && <p>{error?.message}</p>}
    </>
  );
};

export default AllTodos;
