import React, { useState } from "react";
import dayjs from "dayjs";
import relativeTime from "dayjs/plugin/relativeTime";
import { Todo } from "../../types/types";

dayjs.extend(relativeTime);

interface TodoCardProps {
  todo: Todo;
  onDelete: (id: number) => void;
  onUpdate: (id: number, data: Partial<Todo>) => void;
}

const TodoCard: React.FC<TodoCardProps> = ({ todo, onDelete, onUpdate }) => {
  const [isEditing, setIsEditing] = useState(false);
  const [title, setTitle] = useState(todo.title);
  const [description, setDescription] = useState(todo.description);

  const handleUpdate = () => {
    onUpdate(todo.id, { title, description });
    setIsEditing(false);
  };

  return (
    <div>
      {isEditing ? (
        <>
          <input 
            type="text" 
            value={title} 
            onChange={(e) => setTitle(e.target.value)} 
          />
          <input 
            type="text" 
            value={description} 
            onChange={(e) => setDescription(e.target.value)} 
          />
          <button onClick={handleUpdate}>Save</button>
          <button onClick={() => setIsEditing(false)}>Cancel</button>
        </>
      ) : (
        <>
          <h2>{todo.title}</h2>
          <h4>Created {dayjs(todo.createdAt).fromNow()}</h4>
          <p>{todo.description}</p>
          <p>Completed: {todo.completed ? "Yes" : "No"}</p>
          <button onClick={() => onDelete(todo.id)}>Delete</button>
          <button onClick={() => onUpdate(todo.id, { completed: !todo.completed })}>
            {todo.completed ? "Mark as Incomplete" : "Mark as Complete"}
          </button>
          <button onClick={() => setIsEditing(true)}>Edit</button>
        </>
      )}
    </div>
  );
};

export default TodoCard;
