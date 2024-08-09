import { create } from "zustand";
import { createSelectors } from "./create-selectors";
import { persist, createJSONStorage } from "zustand/middleware";

interface GlobalState {
  recruitmentId: number;
  setRecruitmentId: (recruitmentId: number) => void;

  steps: number;
  setSteps: (steps: number) => void;

  sidebar: boolean;
  setSidebar: (sidebarState: boolean) => void;

  selectedTitle: string;
  setSelectedTitle: (title: string) => void;

  activeTab: string | null;
  setActiveTab: (activeTab: string | null) => void;

  selectedHeader: string | null;
  setSelectedHeader: (selectedHeader: string | null) => void;

  editDeactiveDate?: string;
  setEditDeactiveDate: (editDeactiveDate?: string) => void;
}

const useGlobalStoreBase = create<GlobalState>()(
  persist(
    (set) => ({
      recruitmentId: 0,
      setRecruitmentId: (recruitmentId: number) => set({ recruitmentId }),

      sidebar: true,
      setSidebar: (sidebarState) => set({ sidebar: sidebarState }),

      selectedTitle: "Forms",
      setSelectedTitle: (title: string) => set({ selectedTitle: title }),

      editDeactiveDate: "2024-06-02 10:49",
      setEditDeactiveDate: (editDeactiveDate) => set({ editDeactiveDate }),

      steps: 0,
      setSteps: (steps: number) => set({ steps }),

      activeTab: "1",
      setActiveTab: (activeTab: string | null) => set({ activeTab }),

      selectedHeader: "",
      setSelectedHeader: (selectedHeader: string | null) =>
        set({ selectedHeader }),
    }),
    {
      name: "global-store",
      storage: createJSONStorage(() => sessionStorage),
    }
  )
);

export const useGlobalStore = createSelectors(useGlobalStoreBase);
