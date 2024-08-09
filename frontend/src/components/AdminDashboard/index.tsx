import React, { useEffect, useState } from "react";
import scss from "./admindashboard.module.scss";
import fetcher from "@/utils/fetcher";
import { Button, Loader, Paper, Text } from "@mantine/core";
import { useRouter } from "next/router";

const leavesCountArray = [
  {
    id: 1,
    title: "Add Product",
    key: "add_product",
    color: "blue",
    path: "/forms/add-product",
  },
  {
    id: 2,
    key: "view_order",
    title: "View Order",
    color: "red",
    path: "/dashboard/my-leaves?status=Rejected",
  },
];

function AdminDashboard() {
  const router = useRouter();
  const [counts, setCounts] = useState<Record<string, any>>({});
  const [ELCLCounts, setELCLCounts] = useState<Record<string, any>>({});

  return (
    <div className="max-w-screen-xl mx-auto">
      <div className={scss.home_content}>
        {leavesCountArray.map((tab) => (
          <Paper withBorder shadow="md" className={scss.count_box} key={tab.id}>
            <div className={scss.card_wrapper}>
              <div className={scss.main_heading}>
                <Text c={tab.color} component="span">
                  {counts[tab.key]}
                </Text>
              </div>
              <div className={scss.title}>
                <Text c={tab.color} component="p" mb="1rem">
                  {tab.title}
                </Text>
              </div>
              {tab.title !== "Remaining" && (
                <Button
                  color={tab.color}
                  onClick={() => router.push(tab.path!)}
                >
                  View List
                </Button>
              )}
            </div>
          </Paper>
        ))}
      </div>
    </div>
  );
}

export default AdminDashboard;
